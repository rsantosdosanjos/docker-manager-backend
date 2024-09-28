package com.resantosdosanjos.docker_manager.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DockerServiceTest {

    private DockerClient dockerClient;
    private DockerService dockerService;

    @BeforeEach
    void setUp() {
        dockerClient = Mockito.mock(DockerClient.class);
        dockerService = new DockerService(dockerClient);
    }

    @Test
    void testListContainers() {
        ListContainersCmd listContainersCmd = Mockito.mock(ListContainersCmd.class);
        Container container = new Container();
        List<Container> mockContainers = List.of(container);

        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(true)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainers);

        List<Container> containers = dockerService.listContainers(true);
        assertEquals(1, containers.size());

        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(true);
        verify(listContainersCmd).exec();
    }

    @Test
    void testListImages() {
        ListImagesCmd listImagesCmd = Mockito.mock(ListImagesCmd.class);
        Image image = new Image();
        List<Image> mockImages = List.of(image);

        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(mockImages);

        List<Image> images = dockerService.listImages();
        assertEquals(1, images.size());

        verify(dockerClient).listImagesCmd();
        verify(listImagesCmd).exec();
    }
}
