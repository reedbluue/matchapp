export interface ISpringError {
  message: string;
  statusCode: number;
  statusName: string;
}

export interface ISpringValidationError extends ISpringError {
  fieldsErrors: ISpringValidationErrorField[];
}

export interface ISpringValidationErrorField {
  field: string;
  message: string;
}