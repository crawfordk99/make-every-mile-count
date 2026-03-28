package model;

import entity.UserEntity;

public interface Vehicle
{
    public String getMake();
    public String getModel();
    public String getYear();
    public String getSubModel();
    public double getCityMpg();
    public UserEntity getOwnerId();
}

