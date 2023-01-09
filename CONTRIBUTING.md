# Contributing

This page contains instructions and guidelines for anybody contributing code to the project.

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change. Please make sure to update tests as appropriate.

## Build

```shell
mvn clean install
```

## Testing

Each package contain a standalone Spring application with a particular configuration to test
(i.e. application with Spring-Security, application with ExceptionMapper etc). If you have 
a feature or a bug, please include another package with a standalone app to prove the validity
of the change.

See the test folder for examples.
