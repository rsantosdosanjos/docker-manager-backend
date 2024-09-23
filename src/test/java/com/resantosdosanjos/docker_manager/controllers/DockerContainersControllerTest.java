package com.resantosdosanjos.docker_manager.controllers;

import com.github.dockerjava.api.model.Container;
import com.resantosdosanjos.docker_manager.services.DockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerContainersControllerTest {

    private DockerService dockerService;
    private DockerContainersController dockerContainersController;

    @BeforeEach
    void setUp() {
        dockerService = Mockito.mock(DockerService.class);
        dockerContainersController = new DockerContainersController(dockerService);
    }

    @Test
    void testListContainers() {
        Container container = new Container();
        List<Container> mockContainers = Collections.singletonList(container);

        Mockito.when(dockerService.listContainers(true)).thenReturn(mockContainers);

        List<Container> containers = dockerContainersController.listContainers(true);
        assertEquals(1, containers.size());
    }

    @Test
    void testStartContainer() {
        dockerContainersController.startContainer("12345");
        Mockito.verify(dockerService).startContainer("12345");
    }

    @Test
    void testStopContainer() {
        dockerContainersController.stopContainer("12345");
        Mockito.verify(dockerService).stopContainer("12345");
    }
}
