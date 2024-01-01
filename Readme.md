 # Micronaut Platform Proxy
![Build](https://github.com/trevorism/micronaut-platform-proxy/actions/workflows/build.yml/badge.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/trevorism/micronaut-platform-proxy)
![GitHub language count](https://img.shields.io/github/languages/count/trevorism/micronaut-platform-proxy)
![GitHub top language](https://img.shields.io/github/languages/top/trevorism/micronaut-platform-proxy)
 
Latest [Version](https://github.com/trevorism/micronaut-platform-proxy/releases/latest)
 
Java library that enables Trevorism apps to act as a proxy to other registered APIs.

 
## How to Use 
Register a proxy API with https://platform.project.trevorism.com

Add this library to classpath. 

Add to application yaml
```yaml
trevorism:
  platform:
    serviceName: <my-service-name>
    enabled: true
```

## How to Build
`gradle clean build`