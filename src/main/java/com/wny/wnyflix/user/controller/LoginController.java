package com.wny.wnyflix.user.controller;



import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.wny.wnyflix.es.EsClient;
import com.wny.wnyflix.user.domain.CountryCode;
import com.wny.wnyflix.user.domain.MemberForm;
import com.wny.wnyflix.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Controller
public class LoginController {

    @Autowired
    private final EsClient esClient = new EsClient();

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String createUser(MemberForm form, Model model) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        ElasticsearchClient client = esClient.client();
        CountryCode countryCode = new CountryCode();

        form.setCountry_iso_code(countryCode.getCountryCode(form.getCountry_name()));

        IndexResponse response = client.index(i -> i
                .index("wy_user_data")
                .id(form.getUserId())
                .document(form)
        );
        model.addAttribute("response", response.toString());
        log.info("response : " + response.toString());


        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, String userId, String password, HttpServletRequest request) {
        try {
        ElasticsearchClient client = esClient.client();
            GetResponse<User> response = client.get(g -> g
                            .index("wy_user_data")
                            .id(userId),
                    User.class
            );

            User user = response.source();
            if(user.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("AUTHUSER", user);
                log.info("logined id={}", user.getUserId());
            }else {
                model.addAttribute("error", "다시 시도하세요");
                return "login";
            }

        }catch (Exception e) {
            log.error("login error : {}", e.toString());
        }

        return "redirect:/movie";
    }

    @GetMapping("/logout")
    public String logout(Model model,HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/loginElastic")
    public String loginElastic(HttpServletRequest request) {
        try {
            ElasticsearchClient client = esClient.client();
            GetResponse<User> response = client.get(g -> g
                            .index("wy_user_data")
                            .id("elastic"),
                    User.class
            );

            User user = response.source();
            HttpSession session = request.getSession();
            session.setAttribute("AUTHUSER", user);
            log.info("logined id={}", user.getUserId());


        }catch (Exception e) {
            log.error("login error : {}", e.toString());
        }

        return "redirect:/movie";
    }

    @GetMapping("/fromSearchUI")
    public String fromSearchUI(HttpServletRequest request, String requestURI) {
        try {
            ElasticsearchClient client = esClient.client();
            GetResponse<User> response = client.get(g -> g
                            .index("wy_user_data")
                            .id("elastic"),
                    User.class
            );

            User user = response.source();
            HttpSession session = request.getSession();
            session.setAttribute("AUTHUSER", user);
            log.info("logined id={}", user.getUserId());


        }catch (Exception e) {
            log.error("login error : {}", e.toString());
        }

        return "redirect:"+requestURI;
    }



}
