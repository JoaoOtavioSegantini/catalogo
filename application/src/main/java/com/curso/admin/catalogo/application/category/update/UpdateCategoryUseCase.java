package com.curso.admin.catalogo.application.category.update;

import com.curso.admin.catalogo.application.UseCase;
import com.curso.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
