import kotlinx.cli.*

fun main(args: Array<String>) {
    val parser = ArgParser("app")

    val login by parser.option(
        ArgType.String,
        fullName = "login",
        description = "User login"
    ).required()

    val password by parser.option(
        ArgType.String,
        fullName = "password",
        description = "User password"
    ).required()

    parser.parse(args)

    println("Login: $login")
    println("Password: $password")
}

