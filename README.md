<div align="center">

  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://sublab.dev/logo_light.png">
    <img alt="Sublab logo" src="https://sublab.dev/logo.png">
  </picture>

</div>

[![Maven Central](https://img.shields.io/maven-central/v/dev.sublab/curve25519-kotlin)](https://mvnrepository.com/artifact/dev.sublab/curve25519-kotlin)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Telegram channel](https://img.shields.io/badge/chat-telegram-green.svg?logo=telegram)](https://t.me/sublabsupport)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Sublab's Curve25519 Kotlin library

This is a Kotlin repository which shares common functionality across most of our repositories which solve separate problems in Substrate ecosystem.

So this is a good place to store new types that do not exist in Kotlin environment, but required for Substrate to work, such as Rust-specific types; some syntax sugar to enhance development speed and code readability; and different conversions between multiple types that happens frequently.

## Sublab

At Sublab we're making mobile-first libraries for developers in [Substrate](https://substrate.io) ecosystem. However, we seek our libraries to be available not only for mobile Apple OS or Android OS, but compatible with any Swift/Kotlin environment: web servers, desktop apps, and whatnot.

Our mission is to to develop fully native open-source libraries for mobile platforms in Polkadot and Kusama ecosystems, covering everything with reliable unit-tests and providing rich documentation to the developers community. 

Our goal is to have more developers to come into the world of development of client applications in Substrate ecosystem, as we find this as most promising and intelligent blockchain project we ever seen. Thus, we as mobile development gurus trying to create enormously professional libraries which might be very complicated under the hood, but very simple and convenient for final developers.

## Installation

This library is available at Maven Central. Feel free to copy and paste code blocks below to integrate it to your application.

**build.gradle**

```groovy
repositories {
    mavenCentral()
}
```

**Maven**

```xml
<dependency>
    <groupId>dev.sublab</groupId>
    <artifactId>curve25519-kotlin</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle**

```groovy
implementation 'dev.sublab:curve25519-kotlin:1.0.0'
```

## Documentation

- Our GitBook: [https://docs.sublab.dev/kotlin-libraries/curve25519](https://docs.sublab.dev/kotlin-libraries/curve25519)
- API reference: [https://api-reference.sublab.dev/curve25519-kotlin/](https://api-reference.sublab.dev/curve25519-kotlin/)

## Contributing

Please look into our [contribution guide](CONTRIBUTING.md) and [code of conduct](CODE_OF_CONDUCT.md) prior to contributing.

## Contacts

- Website: [sublab.dev](https://sublab.dev)
- E-mail: [info@sublab.dev](mailto:info@sublab.dev)
- Telegram support channel: [t.me/sublabsupport](t.me/sublabsupport)
- Twitter: [twitter.com/sublabdev](https://twitter.com/sublabdev)
