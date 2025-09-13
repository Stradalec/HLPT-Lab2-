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

    val action by parser.option(
        ArgType.String,
        fullName = "action",
        description = "Type of action wtih file"
    ).required()

    val resource by parser.option(
        ArgType.String,
        fullName = "resource",
        description = "Path to resource"
    ).required()

    val volume by parser.option(
        ArgType.String,
        fullName = "volume",
        description = "Volume of file"
    ).required()


    parser.parse(args)

    println("Login: $login")
    println("Password: $password")
}

