package com.resantosdosanjos.docker_manager.controllers;

import com.github.dockerjava.api.model.Image;
import com.resantosdosanjos.docker_manager.services.DockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerImagesControllerTest {

    private DockerService dockerService;
    private DockerImagesController dockerImagesController;

    @BeforeEach
    void setUp() {
        dockerService = Mockito.mock(DockerService.class);
        dockerImagesController = new DockerImagesController(dockerService);
    }

    @Test
    void testListImages() {
        Image image = new Image();
        List<Image> mockImages = Collections.singletonList(image);

        Mockito.when(dockerService.listImages()).thenReturn(mockImages);

        List<Image> images = dockerImagesController.listImages();
        assertEquals(1, images.size());
    }

    @Test
    void testFilterImages() {
        Image image = new Image();
        List<Image> mockImages = Collections.singletonList(image);

        Mockito.when(dockerService.filterImages("nginx")).thenReturn(mockImages);

        List<Image> images = dockerImagesController.listImages("nginx");
        assertEquals(1, images.size());
    }
}
