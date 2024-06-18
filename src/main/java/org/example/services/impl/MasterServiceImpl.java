package org.example.services.impl;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.entities.Document;
import org.example.entities.MasterAccessRequest;
import org.example.entities.User;
import org.example.enums.ERole;
import org.example.exceptions.*;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.entities.Master;
import org.example.pojo.SignUpRequest;
import org.example.repositories.DocumentRepository;
import org.example.repositories.MasterAccessRequestRepository;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.example.services.UserService;
import org.example.wrappers.PathSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;

    private final DocumentRepository documentRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final MasterAccessRequestRepository masterAccessRequestRepository;

    @Value("${media.photos.limit.count}")
    private int PHOTO_COUNT_LIMIT;
    @Value("${media.documents.limit.count}")
    private int DOCUMENTS_COUNT_LIMIT;
    @Value("${media.path}")
    private String PATH_TO_MEDIA;


    private final List<String> METRO_STATIONS = new ArrayList<>(Arrays.asList("Автово", "Адмиралтейская", "Академическая",
            "Балтийская", "Бухарестская", "Василеостровская", "Владимирская", "Волковская", "Выборгская",
            "Горьковская", "Гостиный двор", "Гражданский проспект", "Девяткино", "Достоевская", "Елизаровская",
            "Звездная", "Звенигородская", "Кировский завод", "Комендантский проспект", "Крестовский остров",
            "Купчино", "Ладожская", "Ленинский проспект", "Лесная", "Ломоносовская", "Маяковская", "Международная",
            "Московская", "Московские ворота", "Нарвская", "Невский проспект", "Новочеркасская", "Обводной канал",
            "Обухово", "Озерки", "Парк Победы", "Парнас", "Петроградская", "Пионерская", "Площадь Александра Невского-1",
            "Площадь Александра Невского-2", "Площадь Восстания", "Площадь Ленина", "Площадь мужества", "Политехническая",
            "Приморская", "Пролетарская", "Проспект Большевиков", "Проспект Ветеранов", "Проспект Просвещения", "Рыбацкое",
            "Садовая", "Сенная площадь", "Спасская", "Спортивная", "Старая Деревня", "Технологический институт",
            "Удельная", "Улица Дыбенко", "Фрунзенская", "Черная речка", "Чернышевская", "Чкаловская", "Электросила"));

    @Override
    public MasterDTO getMaster(Long id) {
        return convertToDTO(masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id)));
    }

    @Override
    public void createMaster(MasterAccessRequest request, Long userId, List<Document> documents) {
        Master master = new Master(
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getTelegramTag(),
                request.getDescription(),
                request.getAge(),
                request.getRate(),
                request.getPhotoLink(),
                userId
        );
        masterRepository.save(master);
        documents.forEach(doc -> {
            doc.setMaster(master);
        });
        documentRepository.saveAll(documents);
    }

    @Override
    public MasterInfoDTO getMasterInfo(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id));
        return MasterInfoDTO.builder()
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(documentRepository.findByMaster(master))
                .build();
    }

    @Override
    public Page<MasterDTO> getAllMasters(Pageable pageable) {
        Page<Master> masters = masterRepository.findAllMastersPage(pageable);
        if (masters.isEmpty()) throw new NoMastersFoundException();
        return masters.map(this::convertToDTO);
    }

    @Override
    public List<String> getMetroStations() {
        return METRO_STATIONS;
    }

    @Override
    public PathSet<String> uploadDocument(List<MultipartFile> multipartFiles, String username) throws IOException { //Todo: Узнать про формат и лимит документов
        PathSet<String> paths = new PathSet<>();
        Files.createDirectories(Paths.get(PATH_TO_MEDIA + username + "/documents/"));
        int countDocuments = countFilesInDirectory(PATH_TO_MEDIA + username + "/documents/");
        if (countDocuments + multipartFiles.toArray().length <= DOCUMENTS_COUNT_LIMIT) {
            multipartFiles.forEach(multipartFile -> {
                String filePath = PATH_TO_MEDIA + username + "/documents/" + multipartFile.getOriginalFilename();
                while (Files.exists(Paths.get(filePath))) {
                    filePath += "(1)";
                }
                File file = new File(filePath);
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();
                if (!Objects.equals(email, "anonymousUser") && email != null)
                    documentRepository.save(
                            new Document(multipartFile.getOriginalFilename(), PATH_TO_MEDIA, masterRepository.findByEmail(email)
                                    .orElseThrow(() -> new MasterNotFoundException(email))
                            )
                    );
                paths.add(file.getAbsolutePath());
            });
            return paths;
        } else {
            throw new DocumentsCountLimitException(countDocuments, DOCUMENTS_COUNT_LIMIT);
        }
    }

    @Override
    public String uploadPhoto(MultipartFile multipartFile, String username) throws IOException { //Todo: Узнать про формат и лимит фото
        Files.createDirectories(Paths.get(PATH_TO_MEDIA + username + "/photo/"));
        int countPhotos = countFilesInDirectory(PATH_TO_MEDIA + username + "/photo/");
        if (countPhotos < PHOTO_COUNT_LIMIT) {
            File file = new File(PATH_TO_MEDIA + username + "/photo/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (!Objects.equals(email, "anonymousUser") && email != null) {
                Master master = masterRepository.findByEmail(email).orElseThrow(() -> new MasterNotFoundException(email));
                master.setPhotoLink(file.getAbsolutePath());
                masterRepository.save(master);
            }
            return file.getAbsolutePath();
        } else {
            throw new PhotoCountLimitException(countPhotos, PHOTO_COUNT_LIMIT);
        }
    }

    private MasterDTO convertToDTO(Master master) {
        return MasterDTO.builder()
                .id(master.getId())
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .email(master.getEmail())
                .phoneNumber(master.getPhoneNumber())
                .telegramTag(master.getTelegramTag())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(documentRepository.findByMaster(master))
                .build();
    }

    private int countFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        return Objects.requireNonNull(directory.listFiles()).length;
    }

    @Override
    public void createMasterAccountRequest(SignUpRequest request) {
        if (userService.userExists(request.getUsername()) || masterAccessRequestRepository.existsByEmail(request.getUsername()))
            throw new UserEmailAlreadyExistsException(request.getUsername());
        MasterAccessRequest masterRequest = masterAccessRequestRepository.save(
                new MasterAccessRequest(
                        request.getFirstName(),
                        request.getMiddleName(),
                        request.getLastName(),
                        request.getUsername(),
                        request.getPassword(),
                        request.getRole(),
                        request.getPhoneNumber(),
                        request.getTelegramTag(),
                        request.getDescription(),
                        request.getAge(),
                        request.getRate(),
                        request.getPhotoLink()
                )
        );
        request.getDocuments().forEach(doc -> {
            doc.setMasterAccessRequest(masterRequest);
            doc.setName(Paths.get(doc.getUrl()).getFileName().toString());
        });
        documentRepository.saveAll(request.getDocuments());
    }

    @Override
    public Long acceptMasterAccessRequest(Long id) {
        MasterAccessRequest request = masterAccessRequestRepository.findById(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));

        User user = User.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.valueOf(request.getRole()))
                .build();

        userService.create(user);
        List<Document> documents = documentRepository.findByMasterAccessRequest(request);
        createMaster(request, user.getId(), documents);
        documents.forEach(doc -> {
            doc.setMasterAccessRequest(null);
        });
        documentRepository.saveAll(documents);
        masterAccessRequestRepository.delete(request);
        return id;
    }

    @Override
    public Long discardMasterAccessRequest(Long id) throws IOException {
        MasterAccessRequest masterAccessRequest = masterAccessRequestRepository.findById(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));
        List<Document> documents = documentRepository.findByMasterAccessRequest(masterAccessRequest);
        deleteMediaByUsername(masterAccessRequest.getEmail());
        documentRepository.deleteAll(documents);
        masterAccessRequestRepository.delete(masterAccessRequest);
        return id;
    }

    @Override
    public Long deleteMaster(String username) throws IOException {
        Master master = masterRepository.findByEmail(username).orElseThrow(() -> new MasterNotFoundException(username));
        List<Document> documents = documentRepository.findByMaster(master);
        Long id = master.getId();
        deleteMediaByUsername(username);
        documentRepository.deleteAll(documents);
        masterRepository.delete(master);
        userService.deleteByEmail(username);
        return id;
    }

    private void deleteMediaByUsername(String username) throws IOException {
        File directory = new File(PATH_TO_MEDIA + username);
        FileUtils.deleteDirectory(directory);
    }
}
