package org.testslotegrator.utils;

import org.testslotegrator.model.PlayerRequestDTO;

import static org.apache.commons.lang3.RandomStringUtils.insecure;
import static org.apache.commons.lang3.RandomStringUtils.secureStrong;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class Generators {

    public static PlayerRequestDTO buildPlayerRequestDTO() {
        String username = insecure().nextAlphabetic(12);
        String localPart = insecure().nextAlphanumeric(10).toLowerCase();
        String domain = insecure().nextAlphabetic(6).toLowerCase();
        String email = localPart + "@" + domain + ".com";
        String name = capitalize(insecure().nextAlphabetic(8).toLowerCase());
        String surname = capitalize(insecure().nextAlphabetic(10).toLowerCase());
        String currencyCode = insecure().nextAlphabetic(3).toUpperCase();
        String password = secureStrong().nextAlphanumeric(4);

        return PlayerRequestDTO.builder()
                .username(username)
                .email(email)
                .name(name)
                .surname(surname)
                .currencyCode(currencyCode)
                .passwordChange(password)
                .passwordRepeat(password)
                .build();
    }
}
