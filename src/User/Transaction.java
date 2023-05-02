package User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Helper.GeneralHelper;

/**
 * This is a class to represent a transaction.
 */
public class Transaction {
    private String mDate = "";
    private String mPayee = "";
    private String mCategory = "";
    private String mNote = "";
    private double mAmount = 0.0;

    public Transaction(String pDate, String pPayee, String pCategory, String pNote, double pAmount) {
        this.mDate = pDate;
        this.mPayee = pPayee;
        // Cleaning up catgeory
        this.mCategory = GeneralHelper.cleanCategoryString(pCategory);
        this.mNote = pNote;
        this.mAmount = pAmount;
    }

    public Transaction(){
    }

    @Override
    public String toString() {
        StringBuilder tStringBuilderWorkingVar = new StringBuilder();
        String tSeparator = System.lineSeparator();

        if(this.mAmount < 0){
            tStringBuilderWorkingVar.append("Withdrawal");
        }
        else{
            tStringBuilderWorkingVar.append("Deposit");
        }

        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Date: ");
        tStringBuilderWorkingVar.append(this.mDate);
        tStringBuilderWorkingVar.append(tSeparator);

        if(this.mAmount < 0){
            tStringBuilderWorkingVar.append("To: ");
        }
        else{
            tStringBuilderWorkingVar.append("From: ");
        }
        if(this.mPayee == null || this.mPayee.equals("")){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{ 
            tStringBuilderWorkingVar.append(this.mPayee);
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Category: ");
        if(this.mCategory == null || this.mCategory.equals("")){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{
            tStringBuilderWorkingVar.append(this.mCategory);
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Note: ");
        if(this.mNote == null || this.mNote.equals("")){
            tStringBuilderWorkingVar.append("N/A");
        }
        else{
            tStringBuilderWorkingVar.append(this.mNote);    
        }
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Amount: ");
        if(this.mAmount < 0){
            double roundedAmount = Math.round(this.mAmount * 100.0) / 100.0;
            tStringBuilderWorkingVar.append("-$" + String.format("%.2f", Math.abs(roundedAmount)));
        }
        else{ 
            double roundedAmount = Math.round(this.mAmount * 100.0) / 100.0;
            tStringBuilderWorkingVar.append("$" + String.format("%.2f", roundedAmount));
 
        }
        tStringBuilderWorkingVar.append(tSeparator);

        return tStringBuilderWorkingVar.toString();
    }

    public String getDateAsString() {
        return this.mDate;
    }

    public Date getDateAsDate() {
        String dateString = getDateAsString();
        if (dateString == null) {
            return null;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("M/d/yyyy");
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
        this.mCategory = GeneralHelper.cleanCategoryString(pCategory);
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
