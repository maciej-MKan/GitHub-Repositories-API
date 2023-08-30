package com.mkan.business.model;

import java.util.List;

public record Repo (String name, Boolean fork, List<Branch> branches) {}
