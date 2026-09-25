# API Specification

## Overview

This document defines the public API contracts for the application.

## Base URL

- Development: `http://localhost:8080`

## Endpoints

### GET /health

Returns application health status.

### POST /example

Creates a resource using the provided payload.

## Response Format

Use consistent JSON structure for success and error responses.

## Error Handling

- Return a clear status code
- Include a machine-readable error code when available
- Provide a helpful message for debugging
