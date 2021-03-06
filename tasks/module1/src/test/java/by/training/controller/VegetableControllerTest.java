package by.training.controller;

import by.training.builder.VegetableFactoryService;
import by.training.entity.Vegetable;
import by.training.parser.VegetableParamsParser;
import by.training.parser.VegetableParamsParserImpl;
import by.training.reader.VegetableAttributesLineReader;
import by.training.reader.VegetableAttributesReader;
import by.training.repository.Repository;
import by.training.repository.VegetableRepository;
import by.training.service.VegetableService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.MalformedInputException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RunWith(JUnit4.class)
public class VegetableControllerTest {

    private VegetableController vegetableController;

    @Before
    public void init() {
        VegetableParamsParser parser = new VegetableParamsParserImpl();
        VegetableFactoryService factoryService = new VegetableFactoryService(parser);
        Repository<Vegetable> repository = new VegetableRepository();
        VegetableService service = new VegetableService(repository);
        VegetableAttributesReader reader = new VegetableAttributesLineReader();
        vegetableController = new VegetableController(factoryService, service, reader);
    }

    @Test
    public void transferFileToObjectsOne() throws IOException, URISyntaxException {
        URI uri = this.getClass().getResource("/one_correct_line.txt").toURI();
        String path = Paths.get(uri).toString();
        vegetableController.transformFileToVegetables(path);
        Assert.assertEquals(1, vegetableController.getAll().size());
    }

    @Test
    public void transferFileToObjectsTwo() throws IOException, URISyntaxException {
        URI uri = this.getClass().getResource("/all_lines_incorrect.txt").toURI();
        String path = Paths.get(uri).toString();
        vegetableController.transformFileToVegetables(path);
        Assert.assertEquals(0, vegetableController.getAll().size());
    }

    @Test
    public void transferFileToObjectsThree() throws IOException, URISyntaxException {
        URI uri = this.getClass().getResource("/correct_and_incorrect_lines.txt").toURI();
        String path = Paths.get(uri).toString();
        vegetableController.transformFileToVegetables(path);
        Assert.assertEquals(4, vegetableController.getAll().size());
    }

    @Test
    public void transferFileToObjectsFour() throws IOException, URISyntaxException {
        URI uri = this.getClass().getResource("/empty_file.txt").toURI();
        String path = Paths.get(uri).toString();
        vegetableController.transformFileToVegetables(path);
        Assert.assertEquals(0, vegetableController.getAll().size());
    }

    @Test(expected = MalformedInputException.class)
    public void transferFileToObjectsFive() throws URISyntaxException, IOException {
        URI uri = this.getClass().getResource("/non_text_file.vp").toURI();
        String path = Paths.get(uri).toString();
        vegetableController.transformFileToVegetables(path);
    }

    @Test(expected = NoSuchFileException.class)
    public void transferFileToObjectsSix() throws IOException {
        String path = "dfgghgfhgfhfgk";
        vegetableController.transformFileToVegetables(path);
    }

}
