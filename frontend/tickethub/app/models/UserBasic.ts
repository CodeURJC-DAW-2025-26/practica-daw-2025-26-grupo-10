export interface UserBasic {
    userID: number,
    email: string,
    username: string,
    admin: boolean,
    version: number,
}

export interface ChangePasswordBasic {
    oldPassword: string,
    newPassword: string,
    confirmationPassword: string
}

export interface ChangeProfileBasic {
    version: number,
    name: string,
    surname: string,
    username: string,
    email: string,
    phone: string,
    age: number
}

//TODO: hacer el dto de la imagen del usuario