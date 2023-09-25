package ro.pds.PaperDisseminationSystem.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import ro.pds.PaperDisseminationSystem.entities.User;
import ro.pds.PaperDisseminationSystem.repositories.UserRepository;
import ro.pds.PaperDisseminationSystem.view.request.LoginDto;
import ro.pds.PaperDisseminationSystem.view.request.SignupDto;
import ro.pds.PaperDisseminationSystem.view.response.JwtDto;
import ro.pds.PaperDisseminationSystem.security.jwt.JwtUtils;
import ro.pds.PaperDisseminationSystem.security.services.UserDetailsImpl;

import static ro.pds.PaperDisseminationSystem.util.Validation.validateSignUpRequest;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtDto(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getMetamaskAddress(),
                    "USER"));
        }
        catch(Exception e){
            return new ResponseEntity<>("Incorrect email or password!", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.CONFLICT);
        }

        if (userRepository.existsByMetamaskId(signUpRequest.getMetamask_id())) {
            return new ResponseEntity<>("Error: Metamask address is already in use!", HttpStatus.CONFLICT);
        }

        // Validate user details
        String validation = validateSignUpRequest(signUpRequest);
        if(!validation.contains("Error")) {
            // Create new user's account
            User user = new User(signUpRequest.getName(),
                    signUpRequest.getMetamask_id(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getJob(),
                    signUpRequest.getCompany());

            userRepository.save(user);

            return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(validation, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //brew services start redis
    @PostMapping("/logout")
    public ResponseEntity<?> invalidateJws(@RequestHeader (name="Authorization") String token) {
        token = token.split(" ")[1];
        try {
            Jedis jedis = new Jedis("localhost");
            jedis.set(token, "invalid");
            jedis.close();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JedisConnectionException e) {
            return new ResponseEntity<>("An error has occurred, please try again later!", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
