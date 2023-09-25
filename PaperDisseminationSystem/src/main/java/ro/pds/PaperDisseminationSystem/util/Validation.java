package ro.pds.PaperDisseminationSystem.util;

import ro.pds.PaperDisseminationSystem.view.request.AddArticleDto;
import ro.pds.PaperDisseminationSystem.view.request.SignupDto;

import java.util.HashMap;
import java.util.Map;

public class Validation {
    public static String validateSignUpRequest(SignupDto signupRequest){
        if (signupRequest.getName().length() <= 3 || signupRequest.getName().length() > 100){
            return "Error! Name must contain between 3 and 100 characters!";
        }
        else if(!signupRequest.getName().matches("^[a-zA-Z\\s]+$")){
            return "Error! Name must not contain special characters!";
        }
        else if(signupRequest.getMetamask_id().length() != 42){
            return "Error! Metamask address is invalid!";
        }
        else if(!signupRequest.getEmail().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
            return "Error! Email is invalid!";
        }
        else if(signupRequest.getPassword().length() < 8){
            return "Error! Password must contain at least 8 characters!";
        }
        else if(signupRequest.getPassword().length() > 20){
            return "Error! Password must contain less than 20 characters!";
        }
        else{
            return "Valid request!";
        }
    }

    public static String validateAddArticleRequest(AddArticleDto addArticleDto){
        if(addArticleDto.getTitle().length() == 0){
            return "Error! Title must be present!";
        }
        else if(addArticleDto.getDescription().length() == 0){
            return "Error! Description must be present!";
        }
        else if(addArticleDto.getTagLevels().size() < 1 ){
            return "Error! You have to select at least one tag!";
        }
        else if(addArticleDto.getTagLevels().size() > 3){
            return "Error! You cannot select more than three tags!";
        }
        else{
            return "Valid request!";
        }
    }

}
