import { Injectable, ErrorHandler } from '@angular/core';
import * as Sentry from '@sentry/browser';
import * as StackTrace from 'stacktrace-js';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class SentryErrorHandler implements ErrorHandler {

  lastError: any;

  handleError(error: any) {
    if (error !== this.lastError) {
      if (error instanceof HttpErrorResponse) {
        console.warn(new Date().toISOString(),
          'HTTP error.',
          error.message, 'Status code:',
          (<HttpErrorResponse>error).status);
      } else {
        StackTrace.fromError(error)
           .then((stackframes) => this.sendSentryEvent(stackframes, error));
      }
    }
    this.lastError = error;

    throw error;
  }

  sendSentryEvent(stackframes, error) {

    Sentry.configureScope(scope => {
       scope.setExtra('error_original', error);
    });

    const message = error.message || 'NA - ERRO';
    const eventId = Sentry.captureEvent({
      message,
      timestamp: new Date().getTime(),
      stacktrace: this.buildStackTrace(stackframes)
    });
  }

  buildStackTrace(stackframes): Sentry.Stacktrace {
    const frames = stackframes.map((sf) => {
      return {
        filename: sf.fileName,
        function: sf.functionName,
        lineno: sf.lineNumber,
        colno: sf.columnNumber,
        abs_path: sf.source,
        in_app: !sf.fileName.includes('node_modules')
      };
    });

    return {frames};
  }

}
