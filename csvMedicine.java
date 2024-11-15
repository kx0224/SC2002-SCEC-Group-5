package sc2002.group5.oopprojectsrc;
import java.util.*;
import java.io.*;
public class csvMedicine{
    String selectedcsv;
   Scanner sc=new Scanner(System.in);
   boolean check=false;
   ArrayList<String[]> MedicineList=new ArrayList<>();
   String[][] unparseddata=null;
   String[] entry=null;
    ArrayList<String> medicinenameStrings=new ArrayList<>();
    ArrayList<Float> stockquantity=new ArrayList<>();
    ArrayList<Float>  lowstockalert=new ArrayList<>();
    String Line=null;
    int i=0;
    public csvMedicine(){
        this.selectedcsv=null;
           System.out.println("Initializing Medicine Database....");
           try (BufferedReader reader = new BufferedReader(new InputStreamReader(
               getClass().getResourceAsStream("/Medicine_List.csv")))) {
           while ((Line = reader.readLine()) != null) {
               entry=Line.split(",");
               MedicineList.add(entry);
           }
           unparseddata=MedicineList.toArray(new String[0][]);
           for (i=0;i<unparseddata.length;i++){
            medicinenameStrings.add(unparseddata[i][0]);
            stockquantity.add(Float.parseFloat(unparseddata[i][1]));
            lowstockalert.add(Float.parseFloat(unparseddata[i][2]));

           }
           
           }catch (FileNotFoundException e) {
               System.out.println("File Medicine_list.csv not found. Please check the file path and working directory.");
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       public Medicine getmedicine(){
        String medname;
        while(check==false){
        System.out.println("Enter name of Medicine");
        medname=sc.nextLine();
        sc.nextLine();
        for (i=0;i<medicinenameStrings.size();i++){
            if (medicinenameStrings.get(i).equalsIgnoreCase(medname)){check=true; break;}
        }
        if (!check) {
            System.out.println("Medicine not found. Please try again.");
        }}
        if (stockquantity.get(i)<=lowstockalert.get(i)) System.out.println("Medication on low stock, restocking advised");
        return new Medicine(medicinenameStrings.get(i), stockquantity.get(i), lowstockalert.get(i));
       }
       public void updatedata(Medicine M){
        for (i=0;i<medicinenameStrings.size();i++){
            if (medicinenameStrings.get(i).equalsIgnoreCase(M.getName())){
                stockquantity.set(i, M.getStock());
                lowstockalert.set(i, M.getLowStockAlert());
                break;}
        }
        System.out.println("Medicine Data updated");
       }
       public void updateCSV(){
        String celldata=null;
        for (i=0;i<unparseddata.length;i++){
            unparseddata[i][0]=medicinenameStrings.get(i);
            unparseddata[i][1]=stockquantity.get(i).toString();
            unparseddata[i][2]=lowstockalert.get(i).toString();

        }
        for (i=0;i<unparseddata.length;i++){
            MedicineList.set(i, unparseddata[i]);
        }
        for (i=unparseddata.length;i<medicinenameStrings.size();i++){
            String[] newmedicine=new String[3];
            newmedicine[0]=medicinenameStrings.get(i);
            newmedicine[1]=Float.toString(stockquantity.get(i));
            newmedicine[2]=Float.toString(lowstockalert.get(i));
            MedicineList.add(newmedicine);
        }
        System.out.println("Updating CSV file.....");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Medicine_List.csv"))) {
            for (i=0;i<MedicineList.size();i++) {
                celldata=String.join(",", MedicineList.get(i))+System.lineSeparator();
                writer.write(celldata);

            }
            System.out.println("Medicine_List.csv overwritten with new data.");
        } catch (IOException e) {
            e.printStackTrace();
        }
       }
       public void addMedicine(){
        String name;
        Float quantity;
        Float lowstockalert1;
        System.out.println("Enter name of medication:");
        name=sc.nextLine();
        sc.nextLine();
        System.out.println("Enter current stock quantity, in grams:");
        quantity=sc.nextFloat();
        System.out.println("Enter low stock alert quantity:");
        lowstockalert1=sc.nextFloat();
        medicinenameStrings.add(name);
        stockquantity.add(quantity);
        lowstockalert.add(lowstockalert1);
        System.out.println("Medicine added sucessfully");
       }

    
    
    
}
