package BlockChain.main;
//import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class BlockChain {

    private ArrayList<String> unconfirmedTransactions;
    public static ArrayList<Block> blockChain;
    private int Limit;

    public BlockChain(int Limit) throws Exception {
        this.Limit = Limit;
        unconfirmedTransactions = new ArrayList<String>();
        blockChain = new ArrayList<Block>();
        System.out.println("Loading block from the blockchain file...");
        reReadData();
        System.out.println("Block loaded from the file successfully, checking if the blockchain is valid...");
        if (!isChainValid()) {
            throw new Exception("Invalid blockchain please delete it and re-run the program.");
        }
        System.out.println("Blockchain is valid!");
    }

    public void reReadData() {
//        blockChain.clear();
//        ArrayList<String> lines = FileManager.readFile();
//        for(int i = 0; i < lines.size(); i+=2) {
//            String[] headerSplit = lines.get(i).split("@");
//            Header header = new Header(headerSplit[0], headerSplit[1], headerSplit[2], Integer.parseInt(headerSplit[4]));
//            header.setTimeStamp(Long.parseLong(headerSplit[3]));
//            header.setNonce(Integer.parseInt(headerSplit[5]));
//
//            String[] bodySplit = lines.get(i+1).split("@");
//            ArrayList<String> transactions = new ArrayList<>();
//            String[] tempTransactions = bodySplit[2].substring(1, bodySplit[2].length() - 1).split(", ");
//            for(String transaction : tempTransactions) {
//                transactions.add(transaction);
//            }
//            Block block = new Block(Integer.parseInt(bodySplit[0]), header, transactions);
//            block.setHash(bodySplit[3]);
//            blockChain.add(block);
//        }
    }

    public void addNewTransaction(String transaction) {
        this.unconfirmedTransactions.add(transaction);
        if (unconfirmedTransactions.size() == Limit)
            mineBlock();
    }

    public Block getLastBlock() {
        if (blockChain.size() < 1) {
            return null;
        } else {
            return blockChain.get(blockChain.size() - 1);
        }
    }

    public int GetSize() {
        if (isChainValid())
            return blockChain.size();
        return -1;
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getHeader().getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

    public boolean addBlock(Block block, String proof) {
        if (blockChain.size() < 1) {
            block.getHeader().setPreviousHash("0");
        } else {
            block.getHeader().setPreviousHash(this.getLastBlock().getHash());
        }

        if (this.blockChain.size() > 0) {
            if (!getLastBlock().getHash().equals(block.getHeader().getPreviousHash())) {
                return false;
            }
        }

        block.setHash(proof);
        this.blockChain.add(block);
        return true;
    }

    public String proofOfWork(Block block) {
        String proof = "";
        String target = new String(new char[block.getHeader().getDifficulty()]).replace('\0', '0');
        System.out.println("Mining Block...");
        while (!block.getHash().substring(0, block.getHeader().getDifficulty()).equals(target)) {
            reReadData();
            block.getHeader().setNonce(block.getHeader().getNonce() + 1);
            proof = block.calculateHash();
            block.setHash(proof);
        }
        System.out.println("Mined Block Hash: " + block.getHash());
        return proof;
    }

    public int mineBlock() {
        if (unconfirmedTransactions.size() < 1) {
            return -1;
        }
//        reReadData();
        Header newHeader;
        Block newBlock;
        if (blockChain.size() < 1) {
            newHeader = new Header("1.0", "0", "0", 3);
            newBlock = new Block(0, newHeader, unconfirmedTransactions);
        } else {
            Block lastBlock = getLastBlock();
            newHeader = new Header("1.0", lastBlock.getHash(), "0", 3);
            newBlock = new Block(lastBlock.getIndex() + 1, newHeader, unconfirmedTransactions);
        }

        String proof = this.proofOfWork(newBlock);
        this.addBlock(newBlock, proof);
        this.unconfirmedTransactions.clear();
        return newBlock.getIndex();
    }

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

//    public void blocksExplorer() {
//        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(this.blockChain);
//        System.out.println(blockChainJson);
//    }

    public Block getBlock(int index) {
        return this.blockChain.get(index);
    }
}