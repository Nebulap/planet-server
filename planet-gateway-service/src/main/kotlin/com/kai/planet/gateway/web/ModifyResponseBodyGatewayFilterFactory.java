//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kai.planet.gateway.web;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyDecoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.MessageBodyEncoder;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.GatewayToStringStyler;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
public class ModifyResponseBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<ModifyResponseBodyGatewayFilterFactory.Config> {
    private final Map<String, MessageBodyDecoder> messageBodyDecoders;
    private final Map<String, MessageBodyEncoder> messageBodyEncoders;
    private final List<HttpMessageReader<?>> messageReaders;

    public ModifyResponseBodyGatewayFilterFactory(List<HttpMessageReader<?>> messageReaders, Set<MessageBodyDecoder> messageBodyDecoders, Set<MessageBodyEncoder> messageBodyEncoders) {
        super(Config.class);
        this.messageReaders = messageReaders;
        this.messageBodyDecoders = (Map) messageBodyDecoders.stream().collect(Collectors.toMap(MessageBodyDecoder::encodingType, Function.identity()));
        this.messageBodyEncoders = (Map) messageBodyEncoders.stream().collect(Collectors.toMap(MessageBodyEncoder::encodingType, Function.identity()));
    }

    public GatewayFilter apply(Config config) {
        ModifyResponseGatewayFilter gatewayFilter = new ModifyResponseGatewayFilter(config);
        gatewayFilter.setFactory(this);
        return gatewayFilter;
    }

    public static class Config {
        private Class inClass;
        private Class outClass;
        private Map<String, Object> inHints;
        private Map<String, Object> outHints;
        private String newContentType;
        private RewriteFunction rewriteFunction;

        public Config() {
        }

        public Class getInClass() {
            return this.inClass;
        }

        public Config setInClass(Class inClass) {
            this.inClass = inClass;
            return this;
        }

        public Class getOutClass() {
            return this.outClass;
        }

        public Config setOutClass(Class outClass) {
            this.outClass = outClass;
            return this;
        }

        public Map<String, Object> getInHints() {
            return this.inHints;
        }

        public Config setInHints(Map<String, Object> inHints) {
            this.inHints = inHints;
            return this;
        }

        public Map<String, Object> getOutHints() {
            return this.outHints;
        }

        public Config setOutHints(Map<String, Object> outHints) {
            this.outHints = outHints;
            return this;
        }

        public String getNewContentType() {
            return this.newContentType;
        }

        public Config setNewContentType(String newContentType) {
            this.newContentType = newContentType;
            return this;
        }

        public RewriteFunction getRewriteFunction() {
            return this.rewriteFunction;
        }

        public Config setRewriteFunction(RewriteFunction rewriteFunction) {
            this.rewriteFunction = rewriteFunction;
            return this;
        }

        public <T, R> Config setRewriteFunction(Class<T> inClass, Class<R> outClass, RewriteFunction<T, R> rewriteFunction) {
            this.setInClass(inClass);
            this.setOutClass(outClass);
            this.setRewriteFunction(rewriteFunction);
            return this;
        }
    }

    public class ModifyResponseGatewayFilter implements GatewayFilter, Ordered {
        private final Config config;
        private GatewayFilterFactory<Config> gatewayFilterFactory;

        public ModifyResponseGatewayFilter(Config config) {
            this.config = config;
        }

        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return chain.filter(exchange.mutate().response(ModifyResponseBodyGatewayFilterFactory.this.new ModifiedServerHttpResponse(exchange, this.config)).build());
        }

        public int getOrder() {
            return -2;
        }

        public String toString() {
            Object obj = this.gatewayFilterFactory != null ? this.gatewayFilterFactory : this;
            return GatewayToStringStyler.filterToStringCreator(obj).append("New content type", this.config.getNewContentType()).append("In class", this.config.getInClass()).append("Out class", this.config.getOutClass()).toString();
        }

        public void setFactory(GatewayFilterFactory<Config> gatewayFilterFactory) {
            this.gatewayFilterFactory = gatewayFilterFactory;
        }
    }

    protected class ModifiedServerHttpResponse extends ServerHttpResponseDecorator {
        private final ServerWebExchange exchange;
        private final Config config;

        public ModifiedServerHttpResponse(ServerWebExchange exchange, Config config) {
            super(exchange.getResponse());
            this.exchange = exchange;
            this.config = config;
        }

        private Map<String, Object> decodeBody(String body) {
            return Arrays.stream(body.split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
        }

        private String encodeBody(Map<String, Object> map) {
            return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
        }

        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            Class inClass = this.config.getInClass();
            Class outClass = this.config.getOutClass();
            String originalResponseContentType = (String) this.exchange.getAttribute("original_response_content_type");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", originalResponseContentType);
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            ClientResponse clientResponse = this.prepareClientResponse(body, httpHeaders);
            Mono<String> modifiedBody = this.extractBody(this.exchange, clientResponse, inClass).flatMap((originalBody) -> {
                Map<String, Object> newBodyMap = new HashMap<>();
                newBodyMap.put("userId", "123456");
                if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {

                    // origin body map
                    Map<String, Object> bodyMap = decodeBody(originalBody.toString());

                    // TODO decrypt & auth

                    // new body map


                    return Mono.just(encodeBody(newBodyMap));
                }
                return this.config.getRewriteFunction().apply(this.exchange, newBodyMap);
            }).switchIfEmpty(Mono.defer(() -> {
                return (Mono) this.config.getRewriteFunction().apply(this.exchange, (Object) null);
            }));
            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(this.exchange, this.exchange.getResponse().getHeaders());
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                Mono<DataBuffer> messageBody = this.writeBody(this.getDelegate(), outputMessage, outClass);
                HttpHeaders headers = this.getDelegate().getHeaders();
                if (!headers.containsKey("Transfer-Encoding") || headers.containsKey("Content-Length")) {
                    messageBody = messageBody.doOnNext((data) -> {
                        headers.setContentLength((long) data.readableByteCount());
                    });
                }

                if (StringUtils.hasText(this.config.newContentType)) {
                    headers.set("Content-Type", this.config.newContentType);
                }

                return this.getDelegate().writeWith(messageBody);
            }));
        }

        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return this.writeWith(Flux.from(body).flatMapSequential((p) -> {
                return p;
            }));
        }

        private ClientResponse prepareClientResponse(Publisher<? extends DataBuffer> body, HttpHeaders httpHeaders) {
            ClientResponse.Builder builder = ClientResponse.create(this.exchange.getResponse().getStatusCode(), ModifyResponseBodyGatewayFilterFactory.this.messageReaders);
            return builder.headers((headers) -> {
                headers.putAll(httpHeaders);
            }).body(Flux.from(body)).build();
        }

        private <T> Mono<T> extractBody(ServerWebExchange exchange, ClientResponse clientResponse, Class<T> inClass) {
            if (byte[].class.isAssignableFrom(inClass)) {
                return clientResponse.bodyToMono(inClass);
            } else {
                List<String> encodingHeaders = exchange.getResponse().getHeaders().getOrEmpty("Content-Encoding");
                Iterator var5 = encodingHeaders.iterator();

                MessageBodyDecoder decoder;
                do {
                    if (!var5.hasNext()) {
                        return clientResponse.bodyToMono(inClass);
                    }

                    String encoding = (String) var5.next();
                    decoder = (MessageBodyDecoder) ModifyResponseBodyGatewayFilterFactory.this.messageBodyDecoders.get(encoding);
                } while (decoder == null);

                Mono<byte[]> var10000 = clientResponse.bodyToMono(byte[].class).publishOn(Schedulers.parallel());
                Objects.requireNonNull(decoder);
                return var10000.map(decoder::decode).map((bytes) -> {
                    return exchange.getResponse().bufferFactory().wrap(bytes);
                }).map((buffer) -> {
                    return this.prepareClientResponse(Mono.just(buffer), exchange.getResponse().getHeaders());
                }).flatMap((response) -> {
                    return response.bodyToMono(inClass);
                });
            }
        }

        private Mono<DataBuffer> writeBody(ServerHttpResponse httpResponse, CachedBodyOutputMessage message, Class<?> outClass) {
            Mono<DataBuffer> response = DataBufferUtils.join(message.getBody());
            if (byte[].class.isAssignableFrom(outClass)) {
                return response;
            } else {
                List<String> encodingHeaders = httpResponse.getHeaders().getOrEmpty("Content-Encoding");
                Iterator var6 = encodingHeaders.iterator();

                while (var6.hasNext()) {
                    String encoding = (String) var6.next();
                    MessageBodyEncoder encoder = (MessageBodyEncoder) ModifyResponseBodyGatewayFilterFactory.this.messageBodyEncoders.get(encoding);
                    if (encoder != null) {
                        DataBufferFactory dataBufferFactory = httpResponse.bufferFactory();
                        Mono<byte[]> var10000 = response.publishOn(Schedulers.parallel()).map((buffer) -> {
                            byte[] encodedResponse = encoder.encode(buffer);
                            DataBufferUtils.release(buffer);
                            return encodedResponse;
                        });
                        Objects.requireNonNull(dataBufferFactory);
                        response = var10000.map(dataBufferFactory::wrap);
                        break;
                    }
                }

                return response;
            }
        }
    }
}
