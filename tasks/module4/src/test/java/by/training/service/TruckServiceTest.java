package by.training.service;

import by.training.entity.PlainTruck;
import by.training.entity.PlainWarehouse;
import by.training.model.Truck;
import by.training.model.TruckDefaultComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import by.training.repository.Repository;
import by.training.repository.TruckRepository;
import by.training.repository.WarehouseRepository;

import java.util.Comparator;

@RunWith(JUnit4.class)
public class TruckServiceTest {

    private static final int WAREHOUSE_ID = 1;
    private static final int TRUCK_ID = 0;
    private TruckService truckService;
    private WarehouseService warehouseService;

    @Before
    public void init() {
        Repository<PlainWarehouse> warehouseRepository = new WarehouseRepository();
        Repository<PlainTruck> truckRepository = new TruckRepository();
        warehouseService = new WarehouseService(warehouseRepository);
        Comparator<Truck> defComparator = new TruckDefaultComparator();
        truckService = new TruckService(truckRepository, warehouseService, defComparator);
    }

    @Test
    public void processAll() throws TruckServiceException {
        PlainTruck truck = new PlainTruck(TRUCK_ID, 20, 20, false);
        truckService.create(truck);
        PlainWarehouse warehouse = new PlainWarehouse(WAREHOUSE_ID, 100, 50, 4);
        warehouseService.create(warehouse);

        truckService.processAll(WAREHOUSE_ID);

        int warehouseCargo = warehouseService.get(WAREHOUSE_ID).getCargoWeight();
        Assert.assertEquals(70, warehouseCargo);

        PlainTruck modifiedTruck = truckService.get(TRUCK_ID);
        Assert.assertEquals(0, modifiedTruck.getCargoWeight());
    }

}
