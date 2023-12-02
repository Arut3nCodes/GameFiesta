package com.example.gamefiesta;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {
    @Id
    private String _id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String permissions;
    @Indexed(unique = true)
    private String email;
    



    public Users(String username, String password, String permissions, String email) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.email = email;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        // throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    }




    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }




    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }




    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }




    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }



}
