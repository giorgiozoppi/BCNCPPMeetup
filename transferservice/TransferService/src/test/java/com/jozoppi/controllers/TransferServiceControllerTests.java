/**
 * TransferServiceControllerTests.java
 * @author Giorgio Zoppi
 * @email giorgio.zoppi@gmail.com
 *
 * We provide the integration tests for the transfer controller.
 * both negative and happy case scenario.
 *
 */
package com.jozoppi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jozoppi.models.Account;
import com.jozoppi.models.Transfer;
import com.jozoppi.models.User;
import com.jozoppi.repositories.AccountsRepository;
import com.jozoppi.repositories.UsersRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static com.jozoppi.util.JsonUtil.asJsonString;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransferServiceControllerTests
{
    /**
     *  We provide some hardcoded values to be changed before
     *  the execution of this test.
     */
    private static final String SOURCE_USER = "giorgiozoppi";
    private static final String SOURCE_PASSWORD = "LaNocheEsBella77";
    private static final String JO_ZOPPI_ACCOUNT = "JoZoppiAccount";
    private static final String INGENICO_ACCOUNT = "Ingenico";
    private static final String DESTINATION_USER = "ingenico";
    private static final String DESTINATION_PASSWORD = "Minnie1977";
    private static final String SOURCE_AUTHHEADER = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaW9yZ2lvem9wcGkiLCJleHAiOjE1NDA3NzM1NzV9.W79CK5KbnaCm_HIR3dKu6daCaejAml8D4GC4cVJFARpRv5S8P4nAwuFycP9FzI_T8B2ay0kmQ6f7Lfes1GkC4g";
    private static final String DEST_ACCOUNT_ID = "5bd5f758b86835f98496a3be";
    private static final String SOURCE_ACCOUNT_ID = "5bd5f758b86835f98496a3bd";


    private MockMvc mockMvc;

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private AccountsRepository accountRepository;


    @InjectMocks
    private TransferServiceController transferServiceController;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transferServiceController).build();
    }

    /**
     *  Test the happy case. We prefer use exception instead of errorCodes.
     * @throws Exception In case something wrong.
     */
    @Test
    public void Should_Create_AValidAccount() throws Exception
    {
        // arrange the test
        User sourceUser = CreateSourceUser();
        Account sourceAccount = CreateSourceAccount(sourceUser);

        when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount);
        when(usersRepository.findByUsername(any(String.class))).thenReturn(Optional.of(sourceUser));

        mockMvc.perform(post("/transferservice/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", SOURCE_AUTHHEADER).content(asJsonString(sourceAccount)))
                .andExpect(status().isOk()).
                 andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    /**
     * Test the account creation using the JWT token.
     * @throws Exception An exception during the perform might happen.
     */
    @Test
    public void Should_Fail_WhenCreatingANegativeAccount() throws Exception
    {

        User sourceUser = CreateSourceUser();
        Account sourceAccount = CreateSourceAccount(sourceUser);
        sourceAccount.setBalance(-1);

        when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount);
        when(usersRepository.findByUsername(any(String.class))).thenReturn(Optional.of(sourceUser));

            mockMvc.perform(post("/transferservice/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", SOURCE_AUTHHEADER).content(asJsonString(sourceAccount)))
                        .andExpect(status().isBadRequest());

    }


    /**
     * Test the transfer between accounts.
     * It is important to Mock the security context because the controller
     * will go there to find the username. If the user name is not
     * the user provided will fail the transfer.
     * @throws Exception An exception during the perform might happen.
     */

    @Test
    public void Should_Transfer_BetweenAccountsCorrectly() throws Exception
    {
        //
        // arrange the test


        MockSecurityContext(SOURCE_USER);
        User sourceUser = CreateSourceUser();
        Account sourceAccount = CreateSourceAccount(sourceUser);
        Account destinationAccount = CreateDestinationAccount();
        sourceAccount.setBalance(2500);
        destinationAccount.setBalance(0);
        MockAccounts(sourceAccount, destinationAccount);

        Transfer transfer = CreateTransfer(sourceAccount, destinationAccount, 500);

        // act & assert.

        ResultActions action = mockMvc.perform(post("/transferservice/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", SOURCE_AUTHHEADER).content(asJsonString(transfer)))
                .andExpect(status().isOk());
        String resultString = action.andReturn().getResponse().getContentAsString();
        org.json.JSONObject jsonObject = (org.json.JSONObject)JSONParser.parseJSON(resultString);
        Assert.isTrue(jsonObject.has("sourceId"),"SourceId not present");
        Assert.isTrue(jsonObject.has("amount"),"Amount not present");
        Assert.isTrue(jsonObject.has("destinationId"), "DestinationId not present");

    }

    /**
     *  We shall test the condition in which negative account happens.
     *  We allows that happens and when happens we will rollback the situation.
     * @throws Exception An exception during the perform might happen.
     */
    @Test
    public void Should_Fail_WhenTheAccountOverdraw() throws Exception
    {
        User sourceUser = CreateSourceUser();
        Account sourceAccount = CreateSourceAccount(sourceUser);
        Account destinationAccount = CreateDestinationAccount();
        destinationAccount.setBalance(0);
        MockSecurityContext(SOURCE_USER);
        MockAccounts(sourceAccount, destinationAccount);
        double balance = sourceAccount.getBalance() * 2;
                Transfer transfer = CreateTransfer(sourceAccount, destinationAccount, balance);
        mockMvc.perform(post("/transferservice/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", SOURCE_AUTHHEADER).content(asJsonString(transfer)))
                        .andExpect(status().isBadRequest());

    }

    /**
     * A user shall be logged into the spring for doing a tranfer.
     * It means that it should have a JWT token and done post to /login.
     * @throws Exception An exception during the perform might happen.
     */
   @Test
    public void Should_Fail_WhenAUnloggedUserWithdraw() throws Exception
    {
        User sourceUser = CreateSourceUser();
        MockSecurityContext(SOURCE_USER);
        Account sourceAccount = CreateSourceAccount(sourceUser);
        Account destinationAccount = CreateDestinationAccount();
        MockAccounts(sourceAccount, destinationAccount);
        Transfer transfer = CreateTransfer(sourceAccount, destinationAccount, 4000);
        mockMvc.perform(post("/transferservice/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", SOURCE_AUTHHEADER)
                .content(asJsonString(transfer)))
                .andExpect(status().isBadRequest());


    }
    private User CreateSourceUser()
    {
        User user = new User();
        user.setUsername(SOURCE_USER);
        user.setPassword(SOURCE_PASSWORD);
        return user;
    }
    private  User CreateDestinationUser()
    {
        User user = new User();
        user.setUsername(DESTINATION_USER);
        user.setPassword(DESTINATION_PASSWORD);
        return user;
    }
    private Transfer CreateTransfer(Account source, Account destination, double amount)
    {
        Transfer transfer = new Transfer();
        transfer.setSourceId(source.getIdentifier().toHexString());
        transfer.setDestinationId(destination.getIdentifier().toHexString());
        transfer.setAmount(amount);
        return transfer;

    }
    private void MockAccounts(Account sourceAccount, Account destinationAccount)
    {
        User sourceUser = CreateSourceUser();
        User destinationUser = CreateDestinationUser();

        List<Account> sourceAccountList = new ArrayList<Account>();
        sourceAccountList.add(sourceAccount);

        when(accountRepository.findByUserId(sourceUser.getUsername())).
                thenReturn(sourceAccountList);
        when(accountRepository.findByIdentifier(new ObjectId(SOURCE_ACCOUNT_ID)))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByIdentifier(new ObjectId(DEST_ACCOUNT_ID)))
                .thenReturn(Optional.of(destinationAccount));

        when(usersRepository.findByUsername(sourceUser.getUsername())).thenReturn(Optional.of(sourceUser));
        when(usersRepository.findByUsername(destinationUser.getUsername())).thenReturn(Optional.of(destinationUser));
    }

    /**
     * Here we mock the security context.
     * Since we have configured Spring facilities for the JWT.
     * After logging in the principal of the security context
     * it will be an username to be checked before the transfer.
     * @param username userName to be used.
     */
    private void MockSecurityContext(String username)
    {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(username);
    }

    private Account CreateSourceAccount(User user)
    {
        Account account = new Account();
        account.setBalance(1000);
        account.setUserId(user.getUsername());
        account.setName(JO_ZOPPI_ACCOUNT);
        account.setIdentifier(new ObjectId(SOURCE_ACCOUNT_ID));
        return  account;
    }

    private Account CreateDestinationAccount()
    {
        User user = CreateDestinationUser();
        Account account = new Account();
        account.setBalance(1000);
        account.setUserId(user.getUsername());
        account.setName(INGENICO_ACCOUNT);
        account.setIdentifier(new ObjectId(DEST_ACCOUNT_ID));
        return account;
    }

}