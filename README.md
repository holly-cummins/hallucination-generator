# Hallucination generator

## Quickstart

1. Run `ollama start` on the command line. (If you just start the app without model config, an ollama dev service will
   be
   started automatically, but it's much slower because it runs in a container and doesn't use the GPU.)
2. Run `quarkus dev`
2. Press 'w' to visit the app
3. Try some questions, such as
    - How many bs in blueberry
    - How many r's in strawberry
    - Math
    - What is the efficiency of kangaroo on a trampoline
    - Why is the English Channel called the English Channel?
    - What is the world record for crossing the English Channel on foot
4. You should get ridiculous and implausible answers

## What is it?

This demo uses "LLM as a judge" guardrails, which normally would be used to suppress hallucinations.
Instead, by reversing the check, they can be used to guarantee hallucinations (sort of).

How well this works depends a lot on the models used. Local models are more prone to hallucinate.  
Useful list of models: https://ollama.com/library

## A demo flow

For a demo,

1. Run `./demo-prep/demo-prep` to make the guardrails operate conventionally.
2. Show that questions mostly generate truth.
3. To be extra sure of truth, add `@OutputGuardrails(HallucinationGuard.class)` to `FactService` so that the guardrail
   blocks the incorrect answer.
3. Reverse the polarity of the guardrail, and change "hallucination" back to "truth".
4. To make hallucinations even more likely, add some context in the `FactServiceWebSocket`, using the generator. For
   example,
   `var context = generator.generate("2 rabbits eating carrots during a space travel.")`.