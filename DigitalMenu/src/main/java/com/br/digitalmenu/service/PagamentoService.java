package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import com.br.digitalmenu.model.StatusProdutoPedido;
import com.br.digitalmenu.repository.PedidoRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PagamentoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public ResponseEntity<?> gerarPagamento(Long idPedido) {
        OkHttpClient client = new OkHttpClient();

        if(!pedidoRepository.existsById(idPedido)){
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = pedidoRepository.getReferenceById(idPedido);

        if(!pedido.getStatusPedido().equals(StatusPedido.ABERTO)){
            return ResponseEntity.status(409).body("Pedido cancelado ou já finalizado");
        }

// Phone
        JsonObject phone = new JsonObject();
        phone.addProperty("country", "55");
        phone.addProperty("area", "11");
        phone.addProperty("number", "999999999");
        phone.addProperty("type", "MOBILE");

// Customer
        JsonObject customer = new JsonObject();
        customer.addProperty("name", pedido.getCliente().getNome());
        customer.addProperty("email", pedido.getCliente().getEmail());
// customer.addProperty("tax_id", "12345678909");
        JsonArray phones = new JsonArray();
        phones.add(phone);
        customer.add("phones", phones);

// Items
        JsonArray items = new JsonArray();
        pedido.getProdutoPedidos().stream()
                .filter(produtoPedido -> produtoPedido.getStatus().equals(StatusProdutoPedido.FINALIZADO))
                .forEach(produtoPedido -> {
                    JsonObject item = new JsonObject();
                    item.addProperty("name", produtoPedido.getProduto().getNomeProduto());
                    item.addProperty("quantity", produtoPedido.getQuantidade());

                    // Calcula o unit_amount em centavos corretamente
                    BigDecimal preco = produtoPedido.getProduto().getPreco()
                            .setScale(2, RoundingMode.HALF_UP); //  2 casas decimais
                    int unitAmount = preco.multiply(BigDecimal.valueOf(100))
                            .setScale(0, RoundingMode.HALF_UP) // converte para inteiro em centavos
                            .intValue();
                    item.addProperty("unit_amount", unitAmount);

                    items.add(item);
                });

// Payment methods
        JsonArray paymentMethods = new JsonArray();

        JsonObject debitCard = new JsonObject();
        debitCard.addProperty("type", "DEBIT_CARD");
        paymentMethods.add(debitCard);

        JsonObject creditCard = new JsonObject();
        creditCard.addProperty("type", "CREDIT_CARD");
        paymentMethods.add(creditCard);

        JsonObject pix = new JsonObject();
        pix.addProperty("type", "PIX");
        paymentMethods.add(pix);

// Payment methods configs (limitar crédito à vista)
        JsonArray paymentMethodsConfigs = new JsonArray();
        JsonObject configCredit = new JsonObject();
        configCredit.addProperty("type", "CREDIT_CARD");

        JsonArray configOptions = new JsonArray();
        JsonObject option = new JsonObject();
        option.addProperty("option", "INSTALLMENTS_LIMIT");
        option.addProperty("value", "1");
        configOptions.add(option);

        configCredit.add("config_options", configOptions);
        paymentMethodsConfigs.add(configCredit);

// Pedido JSON final
        JsonObject pedidoJson = new JsonObject();
        pedidoJson.addProperty("reference_id", pedido.getId().toString());
        pedidoJson.add("customer", customer);
        pedidoJson.add("items", items);
        pedidoJson.add("payment_methods", paymentMethods);
        pedidoJson.add("payment_methods_configs", paymentMethodsConfigs);
        pedidoJson.addProperty("redirect_url", "https://sualoja.com.br/obrigado");

// Enviar
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(pedidoJson.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://sandbox.api.pagseguro.com/checkouts")
                .addHeader("Authorization", "Bearer 7558811a-f708-453a-84fe-6397694f85faf478738247b29d252a831944c0d938d06c49-ec83-465c-851e-77af5670ff12")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("Resposta bruta: " + responseBody);

            // Parsear o JSON de resposta
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            // Verificar se tem "links"
            if (jsonResponse.has("links")) {
                JsonArray links = jsonResponse.getAsJsonArray("links");

                for (JsonElement linkElement : links) {
                    JsonObject linkObj = linkElement.getAsJsonObject();
                    String rel = linkObj.get("rel").getAsString();

                    if ("PAY".equals(rel)) {
                        String href = linkObj.get("href").getAsString();
                        System.out.println("URL de pagamento: " + href);
                        // aqui você pode retornar no seu método
                        return ResponseEntity.ok(href);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.badRequest().build();
    }
}

