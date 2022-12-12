# Laoshi++

Laoshi++ is a discord bot to help you learn Chinese with your friends.
It offers the ability to host quiz games that support multiple players where you can practice your pronunciation of Chinese words in both HSK and TOCFL dictionaries.

# Hosting

The recommended way of hosting Laoshi++ yourself is through the [docker image](https://hub.docker.com/r/unlimitedsola/laoshi-plus-plus).

```console
$ export DISCORD_TOKEN=<your-discord-token>
$ docker run -e DISCORD_TOKEN unlimitedsola/laoshi-plus-plus
```

# Contributing

## Dictionaries

The dictionaries are stored in JSON Format inside the [resource folder](./src/main/resources/levels).

You can contribute to the dictionaries by opening a [Pull Request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).
Please make sure your formatting is consistent with the existing dictionaries.

If you're not familiar with GitHub, you could also [create an issue](https://github.com/unlimitedsola/laoshi-plus-plus/issues/new) for dictionary corrections and suggestions.

## Contribute code

Laoshi++ is developed in [Kotlin](https://kotlinlang.org/) using the [JDA Framework](https://github.com/DV8FromTheWorld/JDA).

It is recommended to use Intellij IDEA for your local development. To set up the project, simply open the `build.gradle.kts` file and open it as a project.


# License

Laoshi++ is licensed under [MIT License](./LICENSE)
