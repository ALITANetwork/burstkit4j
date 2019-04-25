package burst.kit.test;

import burst.kit.burst.BurstCrypto;
import burst.kit.entity.BurstAddress;
import burst.kit.entity.BurstID;
import burst.kit.entity.BurstValue;
import burst.kit.entity.response.*;
import burst.kit.entity.response.attachment.ATCreationAttachment;
import burst.kit.entity.response.attachment.MultiOutAttachment;
import burst.kit.entity.response.attachment.MultiOutSameAttachment;
import burst.kit.service.BurstNodeService;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class BurstNodeServiceTest {

    private BurstNodeService burstNodeService;
    private BurstCrypto burstCrypto;

    @Before
    public void setUpBurstServiceTest() {
        burstNodeService = BurstNodeService.getInstance("https://wallet1.burst-team.us:2083");
        burstCrypto = BurstCrypto.getInstance();
    }

    @Test
    public void testBurstServiceGetBlock() {
        BlockResponse blockIDResponse = RxTestUtils.testSingle(burstNodeService.getBlock(TestVariables.EXAMPLE_BLOCK_ID));
        BlockResponse blockHeightResponse = RxTestUtils.testSingle(burstNodeService.getBlock(TestVariables.EXAMPLE_BLOCK_HEIGHT));
        BlockResponse blockTimestampResponse = RxTestUtils.testSingle(burstNodeService.getBlock(TestVariables.EXAMPLE_TIMESTAMP));
        BlockResponse blockTransactionResponse = RxTestUtils.testSingle(burstNodeService.getBlock(new BurstID[]{TestVariables.EXAMPLE_TRANSACTION_ID}));
    }

    @Test
    public void testBurstServiceGetBlockID() {
        BlockIDResponse blockIDResponse = RxTestUtils.testSingle(burstNodeService.getBlockId(TestVariables.EXAMPLE_BLOCK_HEIGHT));
    }

    @Test
    public void testBurstServiceGetBlockchainStatus() {
        BlockchainStatusResponse blockchainStatusResponse = RxTestUtils.testSingle(burstNodeService.getBlockchainStatus());
    }

    @Test
    public void testBurstServiceGetBlocks() {
        BlocksResponse blocksResponse = RxTestUtils.testSingle(burstNodeService.getBlocks(0, 99)); // BRS caps this call at 99 blocks.
        //assertEquals(100, blocksResponse.getBlocks().length);
    }

    @Test
    public void testBurstServiceGetConstants() {
        ConstantsResponse constantsResponse = RxTestUtils.testSingle(burstNodeService.getConstants());
    }

    @Test
    public void testBurstServiceGetAccount() {
        AccountResponse accountResponse = RxTestUtils.testSingle(burstNodeService.getAccount(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountATs() {
        AccountATsResponse accountATsResponse = RxTestUtils.testSingle(burstNodeService.getAccountATs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountBlockIDs() {
        AccountBlockIDsResponse accountBlockIDsResponse = RxTestUtils.testSingle(burstNodeService.getAccountBlockIDs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountBlocks() {
        AccountBlocksResponse accountBlocksResponse = RxTestUtils.testSingle(burstNodeService.getAccountBlocks(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountPublicKey() {
        AccountPublicKeyResponse accountPublicKeyResponse = RxTestUtils.testSingle(burstNodeService.getAccountPublicKey(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountTransactionIDs() {
        AccountTransactionIDsResponse accountTransactionIDsResponse = RxTestUtils.testSingle(burstNodeService.getAccountTransactionIDs(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountTransactions() {
        AccountTransactionsResponse accountTransactionsResponse = RxTestUtils.testSingle(burstNodeService.getAccountTransactions(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAccountWithRewardRecipient() {
        AccountsWithRewardRecipientResponse accountsWithRewardRecipientResponse = RxTestUtils.testSingle(burstNodeService.getAccountsWithRewardRecipient(TestVariables.EXAMPLE_POOL_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceGetAT() {
        ATResponse accountATsResponse = RxTestUtils.testSingle(burstNodeService.getAt(TestVariables.EXAMPLE_AT_ID));
    }

    @Test
    public void testBurstServiceGetAtIDs() {
        AtIDsResponse atIDsResponse = RxTestUtils.testSingle(burstNodeService.getAtIds());
    }

    @Test
    public void testBurstServiceGetTransaction() {
        TransactionResponse transactionIdTransactionResponse = RxTestUtils.testSingle(burstNodeService.getTransaction(TestVariables.EXAMPLE_TRANSACTION_ID));
        TransactionResponse fullHashTransactionResponse = RxTestUtils.testSingle(burstNodeService.getTransaction(TestVariables.EXAMPLE_TRANSACTION_FULL_HASH.getBytes()));

        TransactionResponse multiOutTransactionResponse = RxTestUtils.testSingle(burstNodeService.getTransaction(TestVariables.EXAMPLE_MULTI_OUT_TRANSACTION_ID));
        assertEquals(MultiOutAttachment.class, multiOutTransactionResponse.getAttachment().getType());
        assertEquals(22, ((MultiOutAttachment) multiOutTransactionResponse.getAttachment()).getRecipients().length);

        TransactionResponse multiOutSameTransactionResponse = RxTestUtils.testSingle(burstNodeService.getTransaction(TestVariables.EXAMPLE_MULTI_OUT_SAME_TRANSACTION_ID));
        assertEquals(MultiOutSameAttachment.class, multiOutSameTransactionResponse.getAttachment().getType());
        assertEquals(128, ((MultiOutSameAttachment) multiOutSameTransactionResponse.getAttachment()).getRecipients().length);

        TransactionResponse atCreationTransactionResponse = RxTestUtils.testSingle(burstNodeService.getTransaction(TestVariables.EXAMPLE_AT_CREATION_TRANSACTION_ID));
        assertEquals(ATCreationAttachment.class, atCreationTransactionResponse.getAttachment().getType());
    }

    @Test
    public void testBurstServiceGetTransactionBytes() {
        TransactionBytesResponse transactionBytesResponse = RxTestUtils.testSingle(burstNodeService.getTransactionBytes(TestVariables.EXAMPLE_TRANSACTION_ID));
    }

    @Test
    public void testBurstServiceGenerateTransaction() {
        GenerateTransactionResponse withoutMessage = RxTestUtils.testSingle(burstNodeService.generateTransaction(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromBurst(1), BurstValue.fromBurst(1), 1440));
        GenerateTransactionResponse withStringMessage = RxTestUtils.testSingle(burstNodeService.generateTransactionWithMessage(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromBurst(1), BurstValue.fromBurst(1), 1440, "Test Transaction"));
        GenerateTransactionResponse withBytesMessage = RxTestUtils.testSingle(burstNodeService.generateTransactionWithMessage(TestVariables.EXAMPLE_ACCOUNT_ID, TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromBurst(1), BurstValue.fromBurst(1), 1440, TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes()));
    }

    @Test
    public void testBurstServiceSuggestFee() {
        SuggestFeeResponse suggestFeeResponse = RxTestUtils.testSingle(burstNodeService.suggestFee());
        assertTrue(suggestFeeResponse.getPriority().compareTo(suggestFeeResponse.getStandard()) >= 0);
        assertTrue(suggestFeeResponse.getStandard().compareTo(suggestFeeResponse.getCheap()) >= 0);
    }

    @Test
    public void testBurstServiceGetMiningInfo() {
        MiningInfoResponse miningInfoResponse = RxTestUtils.testObservable(burstNodeService.getMiningInfo(), 1).get(0);
    }

    @Test
    public void testBurstServiceGetMyInfo() {
        MyInfoResponse myInfoResponse = RxTestUtils.testSingle(burstNodeService.getMyInfo());
    }

    @Test
    public void testBurstServiceGetRewardRecipient() {
        RewardRecipientResponse rewardRecipientResponse = RxTestUtils.testSingle(burstNodeService.getRewardRecipient(TestVariables.EXAMPLE_ACCOUNT_ID));
    }

    @Test
    public void testBurstServiceSubmitNonce() {
        SubmitNonceResponse submitNonceResponse = RxTestUtils.testSingle(burstNodeService.submitNonce("example", "0", null));
    }

    @Test
    public void testBurstServiceGenerateMultiOut() {
        Map<BurstAddress, BurstValue> recipients = new HashMap<>();
        recipients.put(burstCrypto.getBurstAddressFromPassphrase("example1"), BurstValue.fromBurst(1));
        recipients.put(burstCrypto.getBurstAddressFromPassphrase("example2"), BurstValue.fromBurst(2));
        GenerateTransactionResponse multiOutResponse = RxTestUtils.testSingle(burstNodeService.generateMultiOutTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromPlanck(753000), 1440, recipients));
    }

    @Test
    public void testBurstServiceGenerateMultiOutSame() {
        Set<BurstAddress> recipients = new HashSet<>();
        recipients.add(burstCrypto.getBurstAddressFromPassphrase("example1"));
        recipients.add(burstCrypto.getBurstAddressFromPassphrase("example2"));
        GenerateTransactionResponse multiOutSameResponse = RxTestUtils.testSingle(burstNodeService.generateMultiOutSameTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromBurst(1), BurstValue.fromPlanck(753000), 1440, recipients));
    }

    @Test
    public void testBurstServiceGenerateCreateATTransaction() {
        byte[] lotteryAt = Hex.decode("1e000000003901090000006400000000000000351400000000000201000000000000000104000000803a0900000000000601000000040000003615000200000000000000260200000036160003000000020000001f030000000100000072361b0008000000020000002308000000090000000f1af3000000361c0004000000020000001e0400000035361700040000000200000026040000007f2004000000050000001e02050000000400000036180006000000020000000200000000030000001a39000000352000070000001b07000000181b0500000012332100060000001a310100000200000000030000001a1a0000003618000a0000000200000020080000000900000023070800000009000000341f00080000000a0000001a78000000341f00080000000a0000001ab800000002000000000400000003050000001a1a000000");
        GenerateTransactionResponse createATResponse = RxTestUtils.testSingle(burstNodeService.generateCreateATTransaction(TestVariables.EXAMPLE_ACCOUNT_PUBKEY.getBytes(), BurstValue.fromBurst(1), 1440, "TestAT", "An AT For Testing", lotteryAt, new byte[0], new byte[0], 0, 0, 0, BurstValue.fromBurst(1)));
    }
}
