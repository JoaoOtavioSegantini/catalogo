package com.curso.admin.catalogo.application.category.create;

import com.curso.admin.catalogo.application.UseCase;
import com.curso.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
