package com.example.notesapp.repository;

import lombok.Getter;
import lombok.Setter;

public abstract class AuhtoricatedRep<T> extends Rep<T> {
    @Getter
    @Setter
    private String jwtToken;

    public AuhtoricatedRep(String jwtToken){
        if(isJwtCorrect(jwtToken)){
            this.jwtToken = jwtToken;
        }
    }
    private boolean isJwtCorrect(String jwtTokenDTO) {
        return jwtToken != null && !jwtToken.isEmpty();
    }
}
