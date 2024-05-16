package com.candles.api.candlesapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.candles.api.candlesapi.model.Candle;
import com.candles.api.candlesapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test the User File DAO class
 * 
 * @author Maria Whala
 */

 @Tag("Persistence-tier")
 public class UserFileDAOTest {
     UserFileDAO userFileDAO;
     User[] testusers;
     ObjectMapper mockObjectMapper;
 
     /**
      * Before each test, we will create and inject a Mock Object Mapper to
      * isolate the tests from the underlying file
      * @throws IOException
      */
     @BeforeEach
     public void setupUserFileDAO() throws IOException {
         mockObjectMapper = mock(ObjectMapper.class);
        
         Candle[] candles = new Candle[3];
         candles = new Candle[3];
         candles[0] = new Candle(99,"Scent 1",14.99,1);
         candles[1] = new Candle(100,"Scent 2",14.99,1);
         candles[2] = new Candle(101,"Scent 3",14.99,1);

         testusers = new User[3];
         testusers[0] = new User(0,"admin",candles,true,"admin_password",null);
         testusers[1] = new User(1,"customer1",candles,false,"test_password",null);
         testusers[2] = new User(2,"customer2",candles,false,"cool_password",null);
 
         // When the object mapper is supposed to read from the file
         // the mock object mapper will return the user array above
         when(mockObjectMapper
             .readValue(new File("doesnt_matter.txt"),User[].class))
                 .thenReturn(testusers);
                 userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
     }
 
     @Test
     public void testGetUsers() throws IOException {
         // Invoke
         User[] users = userFileDAO.getUsers();
 
         // Analyze
         assertEquals(users.length,testusers.length);
         for (int i = 0; i < testusers.length;++i)
             assertEquals(users[i],testusers[i]);
     }
 
     @Test
     public void testFindUsers() throws IOException {
         // Invoke
        User[] testUsers0 = userFileDAO.findUsers("cus");

        // Analyze
        assertEquals(2, testUsers0.length);
        assertEquals(testusers[1],testUsers0[0]);
        assertEquals(testusers[2],testUsers0[1]);
        
        // Invoke
        User[] testUsers1 = userFileDAO.findUsers("2");

        // Analyze
        assertEquals(1, testUsers1.length);
        assertEquals(testusers[2], testUsers1[0]);
     }
 
     @Test
     public void testGetUser() {
         // Invoke
         User admin = userFileDAO.getUser(0);
 
         // Analzye
         assertEquals(admin,testusers[0]);
     }
 
     @Test
     public void testCreateUser() {
         // Setup
         User user = new User(3,"customer3",testusers[0].getCart(),false,"password",null);
 
         // Invoke
         User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                 "Unexpected exception thrown");
 
         // Analyze
         assertNotNull(result);
         User actual = userFileDAO.getUser(user.getId());
         assertEquals(actual.getId(),user.getId());
         assertEquals(actual.getName(),user.getName());
     }
 
     @Test
     public void testUpdateUser() {
         // Setup
         User user = new User(0,"admin",testusers[0].getCart(),true,"password",null);
 
         // Invoke
         User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                 "Unexpected exception thrown");
 
         // Analyze
         assertNotNull(result);
         User actual = userFileDAO.getUser(user.getId());
         assertEquals(actual,user);
     }
 
     @Test
     public void testSaveException() throws IOException{
         doThrow(new IOException())
             .when(mockObjectMapper)
                 .writeValue(any(File.class),any(User[].class));
 
         User user = new User(4,"admin2",testusers[0].getCart(),true,"password",null);
 
         assertThrows(IOException.class,
                         () -> userFileDAO.createUser(user),
                         "IOException not thrown");
     }
 
     @Test
     public void testGetUserNotFound() {
         // Invoke
         User user = userFileDAO.getUser(98);
 
         // Analyze
         assertEquals(user,null);
     }
 
     @Test
     public void testUpdateUserNotFound() {
         // Setup
         User user = new User(9,"customer9",testusers[0].getCart(),false,"password",null);
 
         // Invoke
         User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                                 "Unexpected exception thrown");
 
         // Analyze
         assertNull(result);
     }
 
     @Test
     public void testConstructorException() throws IOException {
         // Setup
         ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
         // We want to simulate with a Mock Object Mapper that an
         // exception was raised during JSON object deseerialization
         // into Java objects
         // When the Mock Object Mapper readValue method is called
         // from the userFileDAO load method, an IOException is
         // raised
         doThrow(new IOException())
             .when(mockObjectMapper)
                 .readValue(new File("doesnt_matter.txt"),User[].class);
 
         // Invoke & Analyze
         assertThrows(IOException.class,
                         () -> new UserFileDAO("doesnt_matter.txt",mockObjectMapper),
                         "IOException not thrown");
     }

    @Test
    public void testDeleteUserSuccess() throws IOException {
        // Setup
        UserFileDAO userFileDAO = Mockito.spy(new UserFileDAO("doesnt_matter.txt", mockObjectMapper));
        int idToDelete = 0;

        // Mock the behavior of users map to contain the user to be deleted
        Mockito.when(userFileDAO.users.containsKey(idToDelete)).thenReturn(true);
        Mockito.doReturn(true).when(userFileDAO).save();

        // Invoke
        boolean deletionResult = userFileDAO.deleteUser(idToDelete);

        // Analyze
        assertTrue(deletionResult);
    }

    @Test
    public void testDeleteUserUserNotFound() throws IOException {
        // Setup
        UserFileDAO userFileDAO = Mockito.spy(new UserFileDAO("doesnt_matter.txt", mockObjectMapper));
        int idToDelete = 99;

        // Mock the behavior of users map to not contain the user to be deleted
        Mockito.when(userFileDAO.users.containsKey(idToDelete)).thenReturn(false);

        // Invoke
        boolean deletionResult = userFileDAO.deleteUser(idToDelete);

        // Analyze
        assertFalse(deletionResult);
    }

    @Test
    public void testDeleteUser_IOException() throws IOException {
        // Setup
        UserFileDAO userFileDAO = Mockito.spy(new UserFileDAO("doesnt_matter.txt", mockObjectMapper));
        int idToDelete = 0;

        // Mock the behavior of users map
        Mockito.when(userFileDAO.users.containsKey(idToDelete)).thenReturn(true);
        Mockito.doThrow(new IOException()).when(userFileDAO).save();

        // Invoke and Analyze
        assertThrows(IOException.class, () -> userFileDAO.deleteUser(idToDelete));
    }
 }
 