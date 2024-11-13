package org.example.services.impl;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.entities.*;
import org.example.exceptions.*;
import org.example.pojo.*;
import org.example.repositories.*;
import org.example.services.EmailService;
import org.example.services.MasterService;
import org.example.services.UserService;
import org.example.wrappers.PathSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;

    private final DocumentRepository documentRepository;

    private final UserService userService;
    private final EmailService emailService;
    private final EmailsPinsRepository emailsPinsRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final MetroStationRepository metroStationRepository;

    @Value("${media.documents.limit.count}")
    private int DOCUMENTS_COUNT_LIMIT;

    @Value("${media.path}")
    private String PATH_TO_MEDIA;

    @Override
    public MasterDTO getMaster(Long id) {
        return convertToDTO(masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id)));
    }

    @Override
    public MasterInfoDTO getMasterInfo(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id));
        return MasterInfoDTO.builder()
                .id(master.getId())
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .metroStation(convertMetroStationsToString(master.getMetroStation()))
                .description(master.getDescription())
                .age(master.getAge())
                .categories(convertCategoriesToString(master.getCategories()))
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
    public Page<MasterDTO> getAllNonVerifiedMasters(Pageable pageable) {
        Page<Master> masters = masterRepository.findAllByIsVerifiedByDocksFalse(pageable);
        if (masters.isEmpty()) throw new NoMastersFoundException();
        return masters.map(this::convertToDTO);
    }

    @Override
    public List<String> getMetroStations() {
        return convertMetroStationsToString(metroStationRepository.findAll());
    }

    @Override
    public String uploadPhotoReg(MultipartFile multipartFile, String username) throws IOException {
        Files.createDirectories(Paths.get(PATH_TO_MEDIA + username + "/photo/"));
        File file = new File(PATH_TO_MEDIA + username + "/photo/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
    }

    @Override
    public PathSet<String> uploadDocument(List<MultipartFile> multipartFiles, String username) throws IOException {
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
                documentRepository.save(
                        new Document(multipartFile.getOriginalFilename(), filePath, masterRepository.findByEmail(username)
                                .orElseThrow(() -> new MasterNotFoundException(username))
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
    public void sendValidationMsgToEmail(String email) {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        emailService.sendSimpleEmail(email, "Подтверждение почты", "Для подтверждения почты введите пин код: " + randomNumber);
        Optional<EmailPin> emailPin = emailsPinsRepository.findByEmail(email);
        if (emailPin.isEmpty()) emailsPinsRepository.save(new EmailPin(email, randomNumber));
        else emailsPinsRepository.save(emailPin.get().setPin(randomNumber));
    }

    @Override
    public String uploadPhoto(MultipartFile multipartFile, String username) throws IOException {
        Master master = masterRepository.findByEmail(username).orElseThrow(() -> new MasterNotFoundException(username));
        if (master.getPhotoAdded()) {
            Path path = Path.of(master.getPhotoLink());
            Files.delete(path);
        }
        Files.createDirectories(Paths.get(PATH_TO_MEDIA + username + "/photo/"));
        File file = new File(PATH_TO_MEDIA + username + "/photo/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        master.setPhotoLink(file.getAbsolutePath());
        master.setPhotoAdded(true);
        masterRepository.save(master);
        return file.getAbsolutePath();
    }


    private MasterDTO convertToDTO(Master master) {
        return MasterDTO.builder()
                .id(master.getId())
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .metroStation(convertMetroStationsToString(master.getMetroStation()))
                .email(master.getEmail())
                .phoneNumber(master.getPhoneNumber())
                .telegramTag(master.getTelegramTag())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .isAccepted(master.getIsAccepted())
                .categories(convertCategoriesToString(master.getCategories()))
                .documents(documentRepository.findByMaster(master))
                .userId(master.getUser().getId())
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
    public Long createMasterAccountRequest(SignUpRequest request, User user) {
        if (masterRepository.existsByEmail(request.getEmail()))
            throw new UserEmailAlreadyExistsException(request.getEmail());
        Master master = masterRepository.save(
                new Master(
                        request.getFirstName(),
                        convertStringsToCategories(request.getCategories()),
                        request.getMiddleName(),
                        request.getLastName(),
                        request.getEmail(),
                        false,
                        convertStringsToMetroStations(request.getMetroStations()),
                        request.getPhoneNumber(),
                        request.getTelegramTag() == null ? "" : request.getTelegramTag(),
                        request.getDescription(),
                        request.getAge(),
                        request.getRate(),
                        request.getPhotoLink(),
                        user
                )
        );
        List<Document> documents = new ArrayList<>();
        request.getDocuments().forEach(docDTO -> {
            documents.add(
                    new Document(
                            Paths.get(docDTO.getUrl()).getFileName().toString(),
                            docDTO.getUrl(),
                            master
                    )
            );
        });
        documentRepository.saveAll(documents);
        return master.getId();
    }

    @Override
    public Long acceptMasterAccessRequest(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));
        master.setIsAccepted(true);
        masterRepository.save(master);
        return id;
    }

    @Override
    public Long discardMasterAccessRequest(Long id) throws IOException {
        Master master = masterRepository.findByIdAndIsAcceptedFalse(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));
        List<Document> documents = documentRepository.findByMaster(master);
        deleteMediaByUsername(master.getEmail());
        documentRepository.deleteAll(documents);
        masterRepository.delete(master);
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

    @Override
    public Long getMasterByUserUsername(String username) {
        Master master = masterRepository.findByEmail(username).orElseThrow(() -> new MasterNotFoundException(username));
        if (!master.getIsAccepted()) throw new MasterAccountNotAcceptedException(master.getId());
        return master.getId();
    }

    @Override
    public InputStream getPhoto(Long id) throws FileNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id: " + id));
        Master master = masterRepository.findByUser(user)
                .orElseThrow(() -> new MasterNotFoundException(user.getUsername()));
        if (master.getPhotoLink() == null) throw new NullPointerException("Photo link is null");
        File file = new File(master.getPhotoLink());
        return new FileInputStream(file);
    }

    private List<String> convertCategoriesToString(List<Category> categories) {
        List<String> stringCategories = new ArrayList<>();
        for (Category category : categories) {
            stringCategories.add(category.getName());
        }
        return stringCategories;
    }

    private List<Category> convertStringsToCategories(List<String> stringCategories) {
        List<Category> categories = new ArrayList<>();
        for (String categoryName : stringCategories) {
            categories.add(categoryRepository.findByName(categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName)));
        }
        return categories;
    }


    private List<MetroStation> convertStringsToMetroStations(List<String> stringMetroStations) {
        List<MetroStation> metroStations = new ArrayList<>();
        for (String metroStation : stringMetroStations) {
            metroStations.add(metroStationRepository.findByName(metroStation).orElseThrow(() -> new MetroStationNotFoundException(metroStation)));
        }
        return metroStations;
    }

    private List<String> convertMetroStationsToString(List<MetroStation> metroStations) {
        List<String> stringMetroStations = new ArrayList<>();
        for (MetroStation metroStation : metroStations) {
            stringMetroStations.add(metroStation.getName());
        }
        return stringMetroStations;
    }

    @Override
    public void validateEmail(String email, Long pin) {
        EmailPin emailPin = emailsPinsRepository.findByEmail(email).orElseThrow(() -> new EmailPinNotFoundException(email));
        if (emailPin.getPin() == pin) {
            emailsPinsRepository.delete(emailPin);
        } else throw new InvalidPinException(pin);
    }

    @Override
    public List<MasterDTO> getTop10MastersByRate() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Master> topMasters = masterRepository.findTop10ByOrderByRateDesc(pageable);
        return topMasters.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Long toBid(BidDTO bidDTO) {
        User user = userRepository.findById(bidDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id: " + bidDTO.getUserId()));
        Master master = masterRepository.findByUser(user)
                .orElseThrow(() -> new MasterNotFoundException(user.getUsername()));
        Task task = taskRepository.findById(bidDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(bidDTO.getTaskId()));
        if (bidRepository.existsByMasterAndTask(master, task))
            throw new BidAlreadyExistsException(bidDTO.getUserId(), bidDTO.getTaskId());
        return bidRepository.save(new Bid(bidDTO.getDateStart(), bidDTO.getDateEnd(), bidDTO.getPrice(), task, master)).getId();
    }

    @Override
    public Long verifyDocks(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id));
        master.setIsVerifiedByDocks(true);
        masterRepository.save(master);
        return id;
    }

    @Override
    public InputStream getPhoto(String key) throws FileNotFoundException {
        File file = new File(key);
        if (!file.exists()) throw new FileNotFoundException("Photo not found");
        return new FileInputStream(file);
    }

    @Override
    public List<GetFeedbackResponse> getFeedbacks(Long id) {
        List<Task> tasks = taskRepository.findAllByMastersUserId(id);
        List<GetFeedbackResponse> feedbacks = new ArrayList<>();
        for (Task task : tasks) {
            feedbacks.add(
                    GetFeedbackResponse.builder()
                            .feedback(task.getFeedback())
                            .rate(task.getRate())
                            .price(task.getPrice())
                            .categoryName(task.getCategory().getName())
                            .clientName(task.getClient().getFirstName())
                            .build()
            );
        }
        return feedbacks;
    }

    @Override
    public Long addVerificationComment(Long id, String comment) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new MasterNotFoundException(id));
        master.setVerificationComment(comment);
        return masterRepository.save(master).getId();
    }

    @Override
    public byte[] getStaticFile(Long id) throws IOException {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new MasterNotFoundException(id));
        Document document = documentRepository.findByMaster(master).get(0);
        return Files.readAllBytes(Paths.get(document.getUrl()));
    }

    private void deleteMediaByUsername(String username) throws IOException {
        File directory = new File(PATH_TO_MEDIA + username);
        FileUtils.deleteDirectory(directory);
    }
}
