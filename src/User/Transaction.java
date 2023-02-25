package User;

/**
 * This is a class to represent a transaction.
 */
public class Transaction {
    private String mDate;
    private String mPayee;
    private String mCategory;
    private String mNote;
    private double mAmount;

    public Transaction(String pDate, String pPayee, String pCategory, String pNote, double pAmount) {
        this.mDate = pDate;
        this.mPayee = pPayee;
        this.mCategory = pCategory;
        this.mNote = pNote;
        this.mAmount = pAmount;
    }

    @Override
    public String toString() {
        StringBuilder tStringBuilderWorkingVar = new StringBuilder();
        String tSeparator = System.lineSeparator();
        tStringBuilderWorkingVar.append("Transaction Information");
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Date: ");
        tStringBuilderWorkingVar.append(this.mDate);
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Payee: ");
        if(this.mPayee.isEmpty()){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{ 
            tStringBuilderWorkingVar.append(this.mPayee);
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Category: ");
        if(this.mCategory.isEmpty()){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{
            tStringBuilderWorkingVar.append(this.mCategory);
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Note: ");
        if(this.mNote.isEmpty()){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{
            tStringBuilderWorkingVar.append(this.mNote);    
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Amount: ");
        tStringBuilderWorkingVar.append(this.mAmount);    
        tStringBuilderWorkingVar.append(tSeparator);

        return tStringBuilderWorkingVar.toString();
    }

    public String getDate() {
        return this.mDate;
    }

    public void setDate(String pDate) {
        this.mDate = pDate;
    }

    public String getPayee() {
        return this.mPayee;
    }

    public void setPayee(String pPayee) {
        this.mPayee = pPayee;
    }

    public String getCategory() {
        return this.mCategory;
    }

    public void setCategory(String pCategory) {
        this.mCategory = pCategory;
    }

    public String getNote() {
        return this.mNote;
    }

    public void setNote(String pNote) {
        this.mNote = pNote;
    }

    public double getAmount() {
        return this.mAmount;
    }

    public void setAmount(double Amount) {
        this.mAmount = Amount;
    }
}
