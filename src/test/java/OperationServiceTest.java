import org.dimamir999.dao.FileDao;
import org.dimamir999.model.KeyValue;
import org.dimamir999.service.OperationService;
import org.dimamir999.service.StringKeyValueConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OperationServiceTest {
    OperationService operationService;
    String key = "key";
    String value = "value";
    String dataFile = "test_data";
    String tempFile = "test_temp";
    String line = key + "|-|" + value;
    KeyValue<String, String> keyValue;

    OperationService operationServiceMock;
    @Mock private FileDao fileDao;
    @Mock private StringKeyValueConverter stringKeyValueConverter;

    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);

        operationService = new OperationService(dataFile, tempFile);
        keyValue = new KeyValue<>(key, value);

        operationServiceMock = new OperationService(dataFile, tempFile, fileDao, stringKeyValueConverter);
        when(fileDao.read(dataFile)).thenReturn("");
        when(fileDao.read(tempFile)).thenReturn("");
        when(stringKeyValueConverter.encode(keyValue)).thenReturn(line);
        when(stringKeyValueConverter.decode(line)).thenReturn(keyValue);
    }

    @After
    public void after() {
        new File(System.getProperty("user.dir") + "/" + dataFile).delete();
        new File(System.getProperty("user.dir") + "/" + tempFile).delete();
    }

    @Test
    public void testCreateOneElementExpectedNormalCreate() throws IOException {
        KeyValue result = operationService.create(keyValue);
        operationService.delete(key);

        assertEquals(keyValue, result);
    }

    @Test
    public void testCreateOneElementWithOccupiedKeyExpectedNoCreate() throws IOException {
        String newValue = "newValue";
        KeyValue<String, String> newKeyValue = new KeyValue<>(key, newValue);

        operationService.create(keyValue);
        KeyValue result = operationService.create(newKeyValue);
        operationService.delete(key);

        assertEquals(null, result);
    }

    @Test
    public void testCreateDependencies() throws IOException {
        operationServiceMock.create(keyValue);

        verify(fileDao, times(2)).read(dataFile);
        verify(fileDao, times(2)).read(tempFile);
        verify(fileDao).append(line, tempFile);
        verify(stringKeyValueConverter).encode(keyValue);

        operationServiceMock.delete(key);
    }

    @Test
    public void testCreateStringKeyValueConverterDependencies() throws IOException {
        String newValue = "newValue";
        KeyValue<String, String> newKeyValue = new KeyValue<>(key, newValue);

        operationServiceMock.create(keyValue);

        when(fileDao.read(tempFile)).thenReturn(line);
        operationServiceMock.create(newKeyValue);

        verify(stringKeyValueConverter).decode(line);

        operationServiceMock.delete(key);
    }

    @Test
    public void testGetOneElementExpectedNormalGet() throws IOException {
        operationService.create(keyValue);
        KeyValue result = operationService.get(key);
        operationService.delete(key);

        assertEquals(keyValue, result);
    }

    @Test
    public void testGetNonExistingElementExpectedNull() throws IOException {
        KeyValue result = operationService.get(key);
        operationService.delete(key);

        assertEquals(null, result);
    }

    @Test
    public void testGetDependencies() throws IOException {
        operationService.create(keyValue);

        when(fileDao.read(tempFile)).thenReturn(line);
        operationServiceMock.get(key);

        verify(fileDao, times(2)).read(dataFile);
        verify(fileDao, times(2)).read(tempFile);
        verify(stringKeyValueConverter).decode(line);

        operationService.delete(key);
    }

    @Test
    public void testUpdateOneElementExpectedNormalUpdate() throws IOException {
        String newValue = "newValue";
        KeyValue<String, String> newKeyValue = new KeyValue<>(key, newValue);

        operationService.create(keyValue);
        KeyValue result = operationService.update(newKeyValue);
        operationService.delete(key);

        assertEquals(keyValue, result);
    }

    @Test
    public void testUpdateNonExistingElementExpectedNull() throws IOException {
        KeyValue result = operationService.update(keyValue);

        assertEquals(null, result);
    }

    @Test
    public void testUpdateDependencies() throws IOException {
        String newValue = "newValue";
        KeyValue<String, String> newKeyValue = new KeyValue<>(key, newValue);
        String newLine = key + "|-|" + newValue;
        operationService.create(keyValue);

        when(fileDao.read(tempFile)).thenReturn(line);
        when(stringKeyValueConverter.encode(newKeyValue)).thenReturn(newLine);
        operationServiceMock.update(newKeyValue);

        verify(fileDao, times(2)).read(dataFile);
        verify(fileDao, times(2)).read(tempFile);
        verify(fileDao).write(newLine, tempFile);
        verify(stringKeyValueConverter).decode(line);
        verify(stringKeyValueConverter).encode(keyValue);
        verify(stringKeyValueConverter).encode(newKeyValue);

        operationService.delete(key);
    }

    @Test
    public void testDeleteOneElementExpectedNormalDelete() throws IOException {
        operationService.create(keyValue);
        KeyValue result = operationService.delete(key);

        assertEquals(keyValue, result);
    }

    @Test
    public void testDeleteOneElementKeyExpectedNoDelete() throws IOException {
        KeyValue result = operationService.delete(key);

        assertEquals(null, result);
    }

    @Test
    public void testDeleteDependencies() throws IOException {
        operationService.create(keyValue);

        when(fileDao.read(tempFile)).thenReturn(line);
        operationServiceMock.delete(key);

        verify(fileDao, times(2)).read(dataFile);
        verify(fileDao, times(2)).read(tempFile);
        verify(fileDao).write(line, tempFile);
        verify(stringKeyValueConverter).decode(line);
        verify(stringKeyValueConverter).encode(keyValue);
    }
}
