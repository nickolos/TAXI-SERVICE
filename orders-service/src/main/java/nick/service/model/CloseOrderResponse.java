package nick.service.model;



public class CloseOrderResponse {

    private Long id;
    private String message = "";

    public CloseOrderResponse( Long id){

        this.id = id;

        this.message =  " Спасибо за заказ №" + id+ "!!!";
    }

}
