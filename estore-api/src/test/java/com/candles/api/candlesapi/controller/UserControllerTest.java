package com.candles.api.candlesapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.candles.api.candlesapi.model.CreditCard;
import com.candles.api.candlesapi.model.User;
import com.candles.api.candlesapi.persistence.UserDAO;
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new userController object and inject
     * a mock user DAO
     */
    @BeforeEach
    public void setupuserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetUser() throws IOException {  // getuser may throw IOException
        // Setup
        User user = new User(99, "User", null, false, "password", null);
        // When the same id is passed in, our mock user DAO will return the user object
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetuserNotFound() throws Exception { // createuser may throw IOException
        // Setup
        int userId = 99;
        // When the same id is passed in, our mock user DAO will return null, simulating
        // no user found
        when(mockUserDAO.getUser(userId)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetuserHandleException() throws Exception { // createuser may throw IOException
        // Setup
        int userId = 99;
        // When getuser is called on the Mock user DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.getUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all userController methods
     * are implemented.
     ****************************************************************/

     @Test
     public void testCreateUser() throws IOException {  // createuser may throw IOException
         // Setup
         User user = new User(99, "User 2", null, false, "password", null);
         // when createuser is called, return true simulating successful
         // creation and save
         when(mockUserDAO.createUser(user)).thenReturn(user);
         when(mockUserDAO.findUsers(user.getName())).thenReturn(new User[]{});
 
         // Invoke
         ResponseEntity<User> response = userController.createUser(user);
 
         // Analyze
         assertEquals(HttpStatus.CREATED,response.getStatusCode());
         assertEquals(user,response.getBody());
     }
 
     @Test
     public void testCreateUserFailed() throws IOException {  // createuser may throw IOException
         // Setup
         User user = new User(99, "User", null, false, "password", null);
         // when createuser is called, return false simulating failed
         // creation and save
         when(mockUserDAO.findUsers(user.getName())).thenReturn(new User[]{user});
 
         // Invoke
         ResponseEntity<User> response = userController.createUser(user);
 
         // Analyze
         assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
     }
 
     @Test
     public void testCreateUserHandleException() throws IOException {  // createuser may throw IOException
         // Setup
         User user = new User(99, "User", null, false, "password", null);
 
         // When createuser is called on the Mock user DAO, throw an IOException
         doThrow(new IOException()).when(mockUserDAO).createUser(user);
         when(mockUserDAO.findUsers(user.getName())).thenReturn(new User[]{});
 
         // Invoke
         ResponseEntity<User> response = userController.createUser(user);
 
         // Analyze
         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
     }
 
     @Test
     public void testUpdateUser() throws IOException { // updateuser may throw IOException
         // Setup
         User user = new User(99, "User", null, false, "password", null);
         // when updateuser is called, return true simulating successful
         // update and save
         when(mockUserDAO.updateUser(user)).thenReturn(user);
         ResponseEntity<User> response = userController.updateUser(user);
         user.setName("NotAUser");
 
         // Invoke
         response = userController.updateUser(user);
 
         // Analyze
         assertEquals(HttpStatus.OK,response.getStatusCode());
         assertEquals(user,response.getBody());
     }
 
     @Test
     public void testUpdateUserFailed() throws IOException { // updateuser may throw IOException
         // Setup
         User user = new User(99, "User", null, false, "password", null);
         // when updateuser is called, return true simulating successful
         // update and save
         when(mockUserDAO.updateUser(user)).thenReturn(null);
 
         // Invoke
         ResponseEntity<User> response = userController.updateUser(user);
 
         // Analyze
         assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
     }
 
     @Test
     public void testUpdateuserHandleException() throws IOException { // updateuser may throw IOException
         // Setup
         User user = new User(99, "User", null, false, "password", null);
         // When updateuser is called on the Mock user DAO, throw an IOException
         doThrow(new IOException()).when(mockUserDAO).updateUser(user);
 
         // Invoke
         ResponseEntity<User> response = userController.updateUser(user);
 
         // Analyze
         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetusers() throws IOException { // getusers may throw IOException
        // Setup
        User[] users = new User[2];
        users[0] = new User(99, "User A", null, false, "password", null);
        users[1] = new User(100,"User B", null, false, "password2", null);
        // When getcandles is called return the candles created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testGetusersHandleException() throws IOException { // getusers may throw IOException
        // Setup
        // When getusers is called on the Mock user DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException { // findusers may throw IOException
        // Setup
        String searchString = "la";
        User[] users = new User[2];
        users[0] = new User(99,"User A", null, false, "password", null);
        users[1] = new User(100,"User B", null, false, "password2", null);
        // When findusers is called with the search string, return the two
        /// users above
        when(mockUserDAO.findUsers(searchString)).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testSearchUsersHandleException() throws IOException { // findusers may throw IOException
        // Setup
        String searchString = "an";
        // When createcandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).findUsers(searchString);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchUserNotFound() throws IOException {
        // Setup
        String name = "nonExistentUser";

        // When findUsers is called on the Mock user DAO, returns null for no user found
        when(mockUserDAO.findUsers(name)).thenReturn(null);

        // Invoke
        ResponseEntity<User[]> response = userController.searchUsers(name);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Setup
        int userId = 123;

        // When deleteUser is called on the Mock user DAO, return true
        when(mockUserDAO.deleteUser(userId)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // Setup
        int userId = 123;

        // When deleteUser is called on the Mock user DAO, return false aka user not found
        when(mockUserDAO.deleteUser(userId)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException {
        // Setup
        int userId = 123;

        // When deleteUser is called on the Mock user DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddCreditCard_Success() throws IOException {
        // Setup
        int userId = 123;
        CreditCard creditCard = new CreditCard("1234567890123456", "123", "12/12", "Alex");
        CreditCard addedCreditCard = new CreditCard("1234567890123456", "123", "12/12", "Alex");
        when(mockUserDAO.addCreditCard(userId, creditCard)).thenReturn(addedCreditCard);

        // Invoke
        ResponseEntity<CreditCard> response = userController.addCreditCard(userId, creditCard);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
    }

    @Test
    public void testAddCreditCard_BadRequest() throws IOException {
        // Setup
        int userId = 123;
        CreditCard creditCard = new CreditCard("1234567890123456", "123", "12/12", "Alex");
        when(mockUserDAO.addCreditCard(userId, creditCard)).thenReturn(null);

        // Invoke
        ResponseEntity<CreditCard> response = userController.addCreditCard(userId, creditCard);

        // Analyze
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be Bad Request");
    }

    @Test
    public void testAddCreditCard_InternalServerError() throws IOException {
        // Setup
        int userId = 123;
        CreditCard creditCard = new CreditCard("1234567890123456", "123", "12/12", "Alex");
        doThrow(new IOException()).when(mockUserDAO).addCreditCard(userId, creditCard);

        // Invoke
        ResponseEntity<CreditCard> response = userController.addCreditCard(userId, creditCard);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be Internal Server Error");
    }

}