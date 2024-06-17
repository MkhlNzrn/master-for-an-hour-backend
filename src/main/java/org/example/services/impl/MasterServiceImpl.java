package org.example.services.impl;


import lombok.RequiredArgsConstructor;
import org.example.entities.Document;
import org.example.entities.MasterAccessRequest;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.entities.Master;
import org.example.exceptions.MasterNotFoundException;
import org.example.exceptions.NoMastersFoundException;
import org.example.repositories.DocumentRepository;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final DocumentRepository documentRepository;

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
        return new ArrayList<>(Arrays.asList(
                "Автово",
                "Адмиралтейская",
                "Академическая",
                "Балтийская",
                "Бухарестская",
                "Василеостровская",
                "Владимирская",
                "Волковская",
                "Выборгская",
                "Горьковская",
                "Гостиный двор",
                "Гражданский проспект",
                "Девяткино",
                "Достоевская",
                "Елизаровская",
                "Звездная",
                "Звенигородская",
                "Кировский завод",
                "Комендантский проспект",
                "Крестовский остров",
                "Купчино",
                "Ладожская",
                "Ленинский проспект",
                "Лесная",
                "Ломоносовская",
                "Маяковская",
                "Международная",
                "Московская",
                "Московские ворота",
                "Нарвская",
                "Невский проспект",
                "Новочеркасская",
                "Обводной канал",
                "Обухово",
                "Озерки",
                "Парк Победы",
                "Парнас",
                "Петроградская",
                "Пионерская",
                "Площадь Александра Невского-1",
                "Площадь Александра Невского-2",
                "Площадь Восстания",
                "Площадь Ленина",
                "Площадь мужества",
                "Политехническая",
                "Приморская",
                "Пролетарская",
                "Проспект Большевиков",
                "Проспект Ветеранов",
                "Проспект Просвещения",
                "Рыбацкое",
                "Садовая",
                "Сенная площадь",
                "Спасская",
                "Спортивная",
                "Старая Деревня",
                "Технологический институт",
                "Удельная",
                "Улица Дыбенко",
                "Фрунзенская",
                "Черная речка",
                "Чернышевская",
                "Чкаловская",
                "Электросила"
        ));
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

}
