package User;

import Helper.GeneralHelper;

public class Budget {
    private String mCategory = "";
    private double mBudgeted = 0.0;
    private double mSpent = 0.0;

    public Budget(String pCategory, double pBudgeted, double pSpent) {
        this.mCategory = GeneralHelper.cleanCategoryString(pCategory);
        this.mBudgeted = pBudgeted;
        this.mSpent = pSpent;
    }

    public Budget(){}

    @Override
    public String toString() {
        StringBuilder tStringBuilderWorkingVar = new StringBuilder();
        String tSeparator = System.lineSeparator();
        tStringBuilderWorkingVar.append("Budget Information");
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Category: ");
        tStringBuilderWorkingVar.append(this.mCategory);
        tStringBuilderWorkingVar.append(tSeparator);

        tStringBuilderWorkingVar.append("Budgeted: $");
        tStringBuilderWorkingVar.append(this.mBudgeted);    
        tStringBuilderWorkingVar.append(tSeparator);

        if(this.mSpent >= 0){
            tStringBuilderWorkingVar.append("Spent: $");
        }
        else{
            tStringBuilderWorkingVar.append("Extra Saved: $");
        }
        tStringBuilderWorkingVar.append(Math.abs(this.mSpent));    
        tStringBuilderWorkingVar.append(tSeparator);

        double tRemaining = this.mBudgeted - this.mSpent;
        tRemaining = Math.round(tRemaining * 100.0) / 100.0;
        tStringBuilderWorkingVar.append("Remaining: $" + tRemaining);
          
        tStringBuilderWorkingVar.append(tSeparator);

        return tStringBuilderWorkingVar.toString();
    }


    public String getCategory() {
        return this.mCategory;
    }

    public void setCategory(String pCategory) {
        this.mCategory = GeneralHelper.cleanCategoryString(pCategory);
    }

    public double getBudgeted() {
        return this.mBudgeted;
    }

    public void setBudgeted(double pBudgeted) {
        this.mBudgeted = pBudgeted;
    }

    public double getSpent() {
        return this.mSpent;
    }

    public void setSpent(double pSpent) {
        this.mSpent = pSpent;
    }

}
