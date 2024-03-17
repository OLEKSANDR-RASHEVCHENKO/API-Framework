import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
    public static void main(String[] args) {

        JsonPath js = new JsonPath(payload.coursPrice());
        //1. Print No of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);
        //2.Print Purchase Amount
        int totalAmount=js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);
        //Print Title of the first course
        String titleFirstCourses=js.get("courses[0].title");
        System.out.println(titleFirstCourses);
        //4. Print All course titles and their respective Prices
        for (int i =0;i<count;i++){
            String courseTitles=js.get("courses["+i+"].title");
            int priceOfCourses=js.get("courses["+i+"].price");
            System.out.println(courseTitles);
            System.out.println(priceOfCourses);
        }
        //5. Print no of copies sold by RPA Course
        System.out.println("5. Print no of copies sold by RPA Course");


        for (int i =0;i<count;i++){
            String courseTitles=js.get("courses["+i+"].title");
            if (courseTitles.equalsIgnoreCase("RPA")){
                //copies sold
                int copies=js.get("courses["+i+"].copies");
                System.out.println(copies);
                break;
            }
        }
        //6. Verify if Sum of all Course prices matches with Purchase Amount
        System.out.println("6. Verify if Sum of all Course prices matches with Purchase Amount");


    }
}
