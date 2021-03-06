package by.training.parser;

import by.training.entity.Device;
import by.training.entity.DeviceProperties;
import by.training.entity.DeviceType;
import by.training.entity.MotherBoard;
import by.training.entity.Mouse;
import by.training.entity.PortType;
import by.training.entity.Processor;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static by.training.parser.ParserConstantsContainer.BUTTONS;
import static by.training.parser.ParserConstantsContainer.CONSUMPTION;
import static by.training.parser.ParserConstantsContainer.COOLER;
import static by.training.parser.ParserConstantsContainer.CRITICAL;
import static by.training.parser.ParserConstantsContainer.DATE;
import static by.training.parser.ParserConstantsContainer.DATE_TIME_FORMAT;
import static by.training.parser.ParserConstantsContainer.GROUP;
import static by.training.parser.ParserConstantsContainer.ID;
import static by.training.parser.ParserConstantsContainer.MOTHERBOARD;
import static by.training.parser.ParserConstantsContainer.MOUSE;
import static by.training.parser.ParserConstantsContainer.NAME;
import static by.training.parser.ParserConstantsContainer.ORIGIN;
import static by.training.parser.ParserConstantsContainer.PERIPHERAL;
import static by.training.parser.ParserConstantsContainer.PORT;
import static by.training.parser.ParserConstantsContainer.PRICE;
import static by.training.parser.ParserConstantsContainer.PROCESSOR;
import static by.training.parser.ParserConstantsContainer.RAM;
import static by.training.parser.ParserConstantsContainer.TYPES;

public class DeviceSAXHandler extends DefaultHandler {
    private static final Logger LOGGER = Logger.getLogger(DeviceSAXHandler.class);
    private List<Device> deviceList;
    private String data;
    private DeviceProperties.Builder attributesBuilder;
    private Device.Builder deviceBuilder;

    DeviceSAXHandler() {
        deviceList = new ArrayList<>();
    }

    List<Device> getDevices() {
        return this.deviceList;
    }

    @Override
    public void startDocument() {
        LOGGER.info("At the document's start...");
    }

    @Override
    public void endDocument() {
        LOGGER.info("At the document's end...");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Optional<DeviceType> optionalDeviceType = DeviceType.fromString(qName);

        if (optionalDeviceType.isPresent()) {
            DeviceType deviceType = optionalDeviceType.get();
            startDeviceBuilding(deviceType, attributes);
        } else if ("types".equals(qName)) {
            startDeviceAttributesBuilding(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case PROCESSOR:
            case MOTHERBOARD:
            case MOUSE:
                Device device = deviceBuilder.build();
                deviceList.add(device);
                break;
            case TYPES:
                DeviceProperties attributesContainer = attributesBuilder.build();
                deviceBuilder = deviceBuilder.setAttributesContainer(attributesContainer);
                break;
            case PORT:
                Optional<PortType> optPortType = PortType.fromString(data);
                if (!optPortType.isPresent()) {
                    LOGGER.error("PortType " + data + " not found.");
                    throw new SAXException("PortType " + data + " not found.");
                }
                PortType portType = optPortType.get();
                attributesBuilder.setPortType(portType);
                break;
            case GROUP:
                attributesBuilder.setGroup(data);
                break;
            case PRICE:
                int price = Integer.parseInt(data);
                deviceBuilder = deviceBuilder.setPrice(price);
                break;
            case ORIGIN:
                deviceBuilder = deviceBuilder.setOrigin(data);
                break;
            case NAME:
                deviceBuilder = deviceBuilder.setName(data);
                break;
            case DATE:
                SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
                Date date;
                try {
                    date = format.parse(data);
                } catch (ParseException e) {
                    LOGGER.error(e);
                    throw new SAXException(e);
                }
                deviceBuilder = deviceBuilder.setManufacturingDate(date);
                break;
            default:
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        data = new String(ch, start, length);
    }

    private void startDeviceBuilding(DeviceType type, Attributes attributes) throws SAXException {
        String id = attributes.getValue(ID);
        String sCritical = attributes.getValue(CRITICAL);
        boolean critical = Boolean.parseBoolean(sCritical);

        switch (type) {
            case PROCESSOR:
                deviceBuilder = new Processor.Builder().setId(id).setCritical(critical);
                startProcessorBuilding(attributes);
                break;
            case MOTHERBOARD:
                deviceBuilder = new MotherBoard.Builder().setId(id).setCritical(critical);
                startMotherboardBuilding(attributes);
                break;
            case MOUSE:
                deviceBuilder = new Mouse.Builder().setId(id).setCritical(critical);
                startMouseBuilding(attributes);
                break;
            default:
                LOGGER.error("DeviceType " + type + " not found.");
                throw new SAXException("DeviceType " + type + " not found.");
        }
    }

    private void startProcessorBuilding(Attributes attributes) {
        String sConsumption = attributes.getValue(CONSUMPTION);
        int consumption = Integer.parseInt(sConsumption);
        deviceBuilder = ((Processor.Builder) deviceBuilder).setConsumption(consumption);
    }

    private void startMotherboardBuilding(Attributes attributes) {
        String sRamPorts = attributes.getValue(RAM);
        int ramPorts = Integer.parseInt(sRamPorts);
        deviceBuilder = ((MotherBoard.Builder) deviceBuilder).setRamSlots(ramPorts);
    }

    private void startMouseBuilding(Attributes attributes) {
        String sButtons = attributes.getValue(BUTTONS);
        int buttons = Integer.parseInt(sButtons);
        deviceBuilder = ((Mouse.Builder) deviceBuilder).setButtons(buttons);
    }

    private void startDeviceAttributesBuilding(Attributes attributes) {
        attributesBuilder = new DeviceProperties.Builder();
        String sPeripheral = attributes.getValue(PERIPHERAL);
        boolean peripheral = Boolean.parseBoolean(sPeripheral);
        String sCooler = attributes.getValue(COOLER);
        boolean cooler = Boolean.parseBoolean(sCooler);
        attributesBuilder = attributesBuilder.setCooler(cooler).setPeripheral(peripheral);
    }

}
