package com.ai.springaidemo.controller;

import com.ai.springaidemo.service.ChatService;
import com.ai.springaidemo.service.ImageService;
import com.ai.springaidemo.service.RecipeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GenAIController {
    private final ChatService chatService;
    private final ImageService imageService;
    private final RecipeService recipeService;

    public GenAIController(ChatService chatService, ImageService imageService, RecipeService recipeService) {
        this.chatService = chatService;
        this.imageService = imageService;
        this.recipeService = recipeService;
    }
    @GetMapping("/ask-ai")
    public String getResponse( @RequestParam String prompt) {
        return chatService.getResponse(prompt);
    }
    @GetMapping("/ask-ai-options")
    public String getResponseOptions( @RequestParam String prompt) {
        return chatService.getResponseOptions(prompt);
    }

    @GetMapping("/generate-image")
    public List<String> generateImages(HttpServletResponse reponse,
                                       @RequestParam String prompt,
                                       @RequestParam(defaultValue = "1") int n,
                                       @RequestParam(defaultValue = "1024") int width,
                                       @RequestParam(defaultValue = "1024") int height) throws IOException {
        ImageResponse image = imageService.generateImage(prompt,width,height,n);
        // One Url
//       String imageurl =  image.getResult().getOutput().getUrl();
//       reponse.sendRedirect(imageurl);

        // use stream for multiple rls

        List<String> imageUrls = image.getResults().stream()
                .map((result -> result.getOutput().getUrl()))
                .toList();

        return imageUrls;
    }
    @GetMapping("recipe-creator")
    public String recipeCreator( @RequestParam String ingredients,
                                 @RequestParam String cuisine,
                                 @RequestParam String dietaryRestriction) {
        return recipeService.createRecipe(ingredients,cuisine,dietaryRestriction);

    }


}
