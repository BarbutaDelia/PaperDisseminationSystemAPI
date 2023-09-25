package ro.pds.PaperDisseminationSystem.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import redis.clients.jedis.Jedis;
import ro.pds.PaperDisseminationSystem.entities.*;
import ro.pds.PaperDisseminationSystem.exceptions.*;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.services.*;
import ro.pds.PaperDisseminationSystem.services.CheckUserReviewService;
import ro.pds.PaperDisseminationSystem.view.request.AddArticleDto;
import ro.pds.PaperDisseminationSystem.view.response.ArticleDto;
import ro.pds.PaperDisseminationSystem.view.response.ArticleUploadedByUserDto;
import ro.pds.PaperDisseminationSystem.view.response.DetailedArticleDto;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static ro.pds.PaperDisseminationSystem.util.Conversion.*;
import static ro.pds.PaperDisseminationSystem.util.Validation.validateAddArticleRequest;

@RestController
@RequestMapping("api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TagService tagService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private TagLevelService tagLevelService;

    @Autowired
    private CheckUserReviewService checkUserReviewService;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @GetMapping("")
    public ResponseEntity<?> getAllArticles(
            @RequestParam Optional<Boolean> paymentStatus,
            @RequestParam Optional<Long> userId,
            @RequestParam Optional<String> searchQuery) {

        List<Article> articles = new ArrayList<>();

        try {
            if (searchQuery.isPresent()) {
                articles = articleService.searchArticles(searchQuery.get());
            } else if (paymentStatus.isPresent() && userId.isPresent()) {
                articles = articleService.listAllUserArticles(userId.get(), paymentStatus.get());
                List<ArticleUploadedByUserDto> articleResponseDtos = getArticleUploadedByUserDtosFromArticles(articles);
                return new ResponseEntity<>(articleResponseDtos, HttpStatus.OK);
            } else if (paymentStatus.isPresent()) {
                if(paymentStatus.get()){
                    return new ResponseEntity<>(fetchPaidArticlesFromCache(), HttpStatus.OK);
                }
                else{
                    articles = articleService.listAllUnpaidArticles();
                }
            } else if (userId.isEmpty()) {
                articles = articleService.listAllArticles();
            }
        } catch (CollectionOfArticlesNotFound e) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }

        List<ArticleDto> articleDtos = getArticleDtosFromArticles(articles);
        return new ResponseEntity<>(articleDtos, HttpStatus.OK);
    }

    private List<ArticleDto> fetchPaidArticlesFromCache() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        try (Jedis jedis = new Jedis("localhost")) {
            String latestArticles = jedis.get("--latest articles");

            if (latestArticles == null) {
                List<Article> articles = articleService.listAllPaidArticles();
                jedis.set("--latest articles", mapper.writeValueAsString(getArticleDtosFromArticles(articles)));
                return getArticleDtosFromArticles(articles);
            } else {
                return mapper.readValue(latestArticles.getBytes(), new TypeReference<List<ArticleDto>>() {});
            }
        } catch (IOException e) {
            return getArticleDtosFromArticles(articleService.listAllPaidArticles());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOneArticle(@PathVariable Long id) {
        try {
            Article article = articleService.getArticle(id);
            DetailedArticleDto articleDto = getDetailedArticleDtoFromArticle(article);
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        } catch (ArticleNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addArticle(MultipartHttpServletRequest request,
                                        @RequestHeader(name = "Authorization") String token) {

        AddArticleDto addArticleDto;
        try {
            addArticleDto = new ObjectMapper().readValue(request.getParameter("addArticleDto"), AddArticleDto.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid request format. Please ensure that the request body is formatted correctly.", HttpStatus.BAD_REQUEST);
        }

        String validation = validateAddArticleRequest(addArticleDto);

        if (!validation.contains("Error")) {
            MultipartFile file = request.getFile("file");
            User uploader;
            try {
                uploader = getUploader(token);
            } catch (UserNotFound e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            List<User> authors;
            try {
                authors = getAuthors(addArticleDto);
            } catch (UserNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }

            List<TagLevel> tagLevels;
            try {
                tagLevels = getTagLevels(addArticleDto);
            } catch (TagNotFound | LevelNotFound | TagLevelNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }

            Path path;
            try {
                path = processFile(file, uploader);
            } catch (IllegalArgumentException | IOException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Article article;
            try {
                article = getArticleFromAddArticleDto(addArticleDto, uploader, authors, tagLevels, path);
                article = articleService.saveArticle(article);
            } catch (DataAccessException e) {
                //representation does not correspond with the content expected by the DB
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
            }

            DetailedArticleDto detailedArticleDto = getDetailedArticleDtoFromArticle(article);
            return new ResponseEntity<>(detailedArticleDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validation, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    private User getUploader(String token) throws UserNotFound {
        token = token.split(" ")[1];
        String email = jwtUtils.getEmailFromJwtToken(token);
        return userService.getUserByEmail(email);
    }

    private List<User> getAuthors(AddArticleDto addArticleDto) throws UserNotFound {
        List<User> authors = new ArrayList<>();
        for (String author : addArticleDto.getAuthors()) {
            authors.add(userService.getUserByEmail(author));
        }
        return authors;
    }

    private List<TagLevel> getTagLevels(AddArticleDto addArticleDto) throws TagNotFound, LevelNotFound, TagLevelNotFound {
        List<TagLevel> tagLevels = new ArrayList<>();
        for (String key : addArticleDto.getTagLevels().keySet()) {
            Tag tag = tagService.getTagByName(key);
            Level level = levelService.getLevelByName(addArticleDto.getTagLevels().get(key));
            TagLevel tagLevel = tagLevelService.getTagLevelByLevelIdAndTagId(tag.getId(), level.getId());
            tagLevels.add(tagLevel);
        }
        return tagLevels;
    }

    private Path processFile(MultipartFile file, User uploader) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("No file was received in the request.");
        }

        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name not found.");
        }
        if (!fileName.endsWith(".pdf")) {
            throw new IllegalArgumentException("Invalid file type. Only .pdf files are allowed.");
        }

        Path path = Paths.get(uploadDirectory + "/" + uploader.getId() + "_" + file.getOriginalFilename());

        Files.write(path, bytes);
        return path;
    }

    @GetMapping("/{articleId}/download-article")
    public ResponseEntity<?> downloadArticle(@PathVariable Long articleId, Authentication authentication) {

        try {
            Article article = articleService.getArticle(articleId);
            boolean isBefore = articleService.getArticle(articleId).getReviewUntil().isBefore(LocalDateTime.now());
            if (isBefore) {
                return downloadFile(article);
            }

            if(authentication != null && authentication.isAuthenticated()){
                HashMap<String, String> tagsAndLevelsNeeded = checkUserReviewService.getTagsAndLevelsNeededByArticleId(articleId);
                List<UserTest> userTests = checkUserReviewService.getLatestUserTestsByUserId(userService.getUserByName(authentication.getName()).getId());
                HashMap<String, String> tagsAndLevelsObtainedByUser = checkUserReviewService.getTagsAndLevelsObtainedByUser(userTests);
                boolean isUserReviewPossible = checkUserReviewService.isUserReviewPossible(tagsAndLevelsNeeded, tagsAndLevelsObtainedByUser);
                if (isUserReviewPossible) {
                    return downloadFile(article);
                }
            } else {
                return new ResponseEntity<>("You cannot download this article!", HttpStatus.FORBIDDEN);
            }

            } catch(MalformedURLException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch(ArticleNotFound e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (UserNotFound e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } catch (CollectionOfUserTestsNotFound e) {
                return new ResponseEntity<>("You cannot download this article!", HttpStatus.FORBIDDEN);
            }
        return new ResponseEntity<>("You cannot download this article!", HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<?> downloadFile(Article article) throws MalformedURLException {
        Path filePath = Paths.get(article.getPath());

        // Create a Resource object to represent the file
        Resource fileResource = new UrlResource(filePath.toUri());
        String fileName = fileResource.getFilename();
        if (fileName != null) {
            fileName = fileName.split("_", 2)[1];
        }
        // Return a ResponseEntity with headers to indicate the file is an attachment
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileResource);
    }
}


