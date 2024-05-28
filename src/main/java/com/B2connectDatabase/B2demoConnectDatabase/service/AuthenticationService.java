package com.B2connectDatabase.B2demoConnectDatabase.service;

import com.B2connectDatabase.B2demoConnectDatabase.dto.request.AuthenticationRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.IntrospectRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.AuthenticationResponse;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.IntrospectResponse;
import com.B2connectDatabase.B2demoConnectDatabase.exception.AppException;
import com.B2connectDatabase.B2demoConnectDatabase.exception.ErrorCode;
import com.B2connectDatabase.B2demoConnectDatabase.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        // cos class JWSV
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        // kieerm tra xem token co ket han chua
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        return  IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    public AuthenticationResponse authenticate(AuthenticationRequest request){
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            boolean authenticated =  passwordEncoder.matches(request.getPassword(),
                    user.getPassword());

            if(!authenticated)
                throw new AppException(ErrorCode.UNAUTHENTICATED);

            var token = generateToken(request.getUsername());

            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();

    }
//    method taoj token
    private String generateToken(String username){
        // tạo header
            // header có ND thuật ton mà ta sử dụng
        JWSHeader header  = new JWSHeader(JWSAlgorithm.HS512);

        // tạo payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("devIT.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();

        // cv vef json
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        // kys token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            logger.error("Can't create token",e);
            throw new RuntimeException(e);
        }
    }
}
