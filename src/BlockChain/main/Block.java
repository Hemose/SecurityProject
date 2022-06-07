package BlockChain.main;

import java.util.ArrayList;

public class Block {

    //index, header, transactions_count, transactions
    private int index;
    private Header header;
    private int transactionsCount;
    private ArrayList<String> transactions;
    private String hash;

    public Block(int index, Header header, ArrayList<String> transactions) {
        this.index = index;
        this.header = header;
        this.transactions = (ArrayList<String>) transactions.clone();
        this.transactionsCount = transactions.size();
        this.hash = this.calculateHash();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<String> transactions) {
        this.transactions = transactions;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                header.getPreviousHash()
                        + Long.toString(header.getTimeStamp())
                        + Integer.toString(header.getNonce())
                        + transactions
        );
        return calculatedhash;
    }

    public boolean isBlockValid() {
        if(this.getHash().equals(this.calculateHash())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if(this == null) {
            return "The Object is null";
        }
        return "Block{" + "index=" + index + ", header=" + header + ", transactionsCount=" + transactionsCount + ", transactions=" + transactions + ", hash=" + hash + '}';
    }


}