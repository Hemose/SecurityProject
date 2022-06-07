package BlockChain.main;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private static Controller controller;
    BlockChain blockChain;
    HashMap<Long, PublicKey> clinicKeys;
    private final String delm = "%#@!";
    private static long id;

    private Controller() throws Exception {
        blockChain = new BlockChain(3);
        clinicKeys = new HashMap<>();
    }

    public static Controller getController() throws Exception {
        if (controller == null)
            controller = new Controller();
        return controller;
    }

    public boolean checkValidity() throws Exception {
        if (!blockChain.isChainValid())
            return false;

        //TODO add checking for the signature for every transaction
        for (int i = 0; i < blockChain.GetSize(); i++) {
            Block cur = blockChain.getBlock(i);
            for (String s : cur.getTransactions()) {
                checkTransaction(s);
            }
        }

        return true;
    }

    public boolean checkTransaction(String transaction) throws Exception {
        String[] tokens = transaction.split(delm);
        if (tokens.length != 3)
            return false;
        String data = tokens[0];
        long cId = Long.parseLong(tokens[1]);
        String hash = tokens[2];
        return checkSign(data, cId, hash);
    }

    public Long registerClinic(PublicKey key) {
        clinicKeys.put(id, key);
        return id++;
    }


    public String decSigned(String msg, PublicKey key) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(msg)), "UTF-8");
    }

    public ArrayList<String> getBlockData(int id) throws Exception {
        if (!checkValidity()) {
            return null;
        }
        if (id == blockChain.GetSize())
            blockChain.mineBlock();
        return transformToNormal(blockChain.getBlock(id).getTransactions());
    }

    private ArrayList<String> transformToNormal(ArrayList<String> transactions) {
        ArrayList<String> res = new ArrayList<>();
        for (String s : transactions) {
            String[] tokens = s.split(delm);
            res.add(tokens[0]);
        }
        return res;
    }

    // DATA delm CID delm Shashed;

    public int addTransaction(String data, long clinicId, String signedHashed) throws Exception {
        if (!checkValidity())
            return -1;

        System.out.println("Adding Transaction for Clinic " + clinicId + " Data: " + data + " Hash: " + signedHashed);
        if (!checkSign(data, clinicId, signedHashed)) {
            return -1; // bad transaction
        }


        int pos = blockChain.GetSize();
        String ToBeAdded = data + delm + clinicId + delm + signedHashed;
        System.out.println("The Total Transaction will be " + ToBeAdded);
        blockChain.addNewTransaction(ToBeAdded);
        return pos;
    }

    public boolean checkSign(String data, long clinicId, String signedHashed) throws Exception {

        String Unsigned = decSigned(signedHashed, clinicKeys.get(clinicId));
        String hashed = StringUtil.applySha256(data);

        return hashed.equals(Unsigned);
    }

}
