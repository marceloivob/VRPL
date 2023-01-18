// Karma configuration file, see link for more information
// https://karma-runner.github.io/1.0/config/configuration-file.html
process.env.CHROME_BIN = require('puppeteer').executablePath()

module.exports = function (config) {
  config.set({
    basePath: '',
    preprocessors: {
      // source files, that you wanna generate coverage for
      // do not include tests or libraries
      'src/**/!(*spec|*mock).js': ['coverage']
    },
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage-istanbul-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma'),
      require('karma-sonarqube-reporter')
    ],
    sonarqubeReporter: {
      basePath: 'src/app',        // test folder
      filePattern: '**/*spec.ts', // test file pattern
      outputFolder: 'reports',    // reports destination
      legacyMode: false,          // report for Sonarqube < 6.2 (disabled)
      encoding: 'utf-8'           // file format
    },
    client:{
      clearContext: false // leave Jasmine Spec Runner output visible in browser
    },
    coverageIstanbulReporter: {
      reports: [ 'html', 'lcovonly' ],
      fixWebpackSourcePaths: true
    },
    angularCli: {
      environment: 'dev'
    },
    customLaunchers: {
      ChromeDebugging: {
        base: 'Chrome',
        flags: [ '--remote-debugging-port=9333' ]
      },
      ChromeHeadlessNoSandbox: {
        base: 'ChromeHeadless',
        flags: [
            '--headless', '--disable-dev-shm-usage',
            '--no-sandbox', // required to run without privileges in docker
            '--user-data-dir=/tmp/chrome-test-profile',
            '--disable-setuid-sandbox',
            '--disable-gpu',
            '--disable-web-security'
        ]
      }
    },
    reporters: ['progress', 'kjhtml', 'coverage-istanbul', 'sonarqube'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['ChromeDebugging', 'ChromeHeadlessNoSandbox'],
    browserDisconnectTimeout: 10000,
    browserDisconnectTolerance: 3,
    browserNoActivityTimeout: 60000,
    singleRun: false
  });
};
