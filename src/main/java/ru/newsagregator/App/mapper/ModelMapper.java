package ru.newsagregator.App.mapper;

public interface ModelMapper<Model, DTO> {
    DTO toDTO(Model model);
    Model toModel(DTO dto);
}
