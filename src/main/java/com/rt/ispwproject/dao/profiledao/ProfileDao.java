package com.rt.ispwproject.dao.profiledao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.Profile;

public interface ProfileDao {

    Profile getProfile(String username, String password) throws DbException;
    Profile getProfileById(int userId) throws DbException;
    Profile getProfileByUsername(String username) throws DbException;
}
