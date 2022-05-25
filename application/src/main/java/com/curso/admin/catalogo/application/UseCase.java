package com.curso.admin.catalogo.application;


import com.curso.admin.catalogo.domain.category.Category;
public abstract class UseCase<IN,OUT> {

   public abstract OUT execute(IN aIn);

}