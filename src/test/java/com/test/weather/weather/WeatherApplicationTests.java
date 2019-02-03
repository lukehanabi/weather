package com.test.weather.weather;

import com.test.weather.weather.controller.DataController;
import com.test.weather.weather.model.WeatherData;
import com.test.weather.weather.service.WeatherDataCacheService;
import com.test.weather.weather.service.WeatherDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherApplicationTests {

	DataController controller = null;
	WeatherDataCacheService cacheService = null;
	WeatherDataService dataService = null;


	@Before
	public void contextLoads() {
		cacheService = mock(WeatherDataCacheService.class);
		dataService = mock(WeatherDataService.class);
		controller = new DataController(cacheService, dataService);
	}

	@Test
	public void getAvergaes() {

		when(cacheService.getDataById(any())).thenReturn(
				new WeatherData("id", 10.1, 1.0, 12.0));

		ResponseEntity response = controller.getDataById(anyString(),anyString(),anyString());

		Mockito.verify(cacheService);

		Assert.notNull(response.getBody());
	}

	@Test
	public void getAveragesNull() {

		when(cacheService.getDataById(any())).thenReturn(
				null);

		ResponseEntity response = controller.getDataById(anyString(),anyString(),anyString());

		//Mockito.verify(cacheService);

		Assert.isNull(response.getBody());
	}

	@Test
	public void getAveragesServiceData() {

		when(dataService.getDataById(any())).thenReturn(
				new WeatherData("id", 10.1, 1.0, 12.0));

		ResponseEntity response = controller.getDataById(anyString(),anyString(),anyString());

//		Mockito.verify(dataService);

		Assert.isNull(response.getBody());
	}

	@Test
	public void getAveragesServiceDataNull() {

		when(dataService.getDataById(any())).thenReturn(
				null);

		ResponseEntity response = controller.getDataById(anyString(),anyString(),anyString());

		Mockito.verify(dataService);

		Assert.isNull(response.getBody());
	}

}

