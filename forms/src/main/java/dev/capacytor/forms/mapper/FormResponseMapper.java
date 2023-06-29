package dev.capacytor.forms.mapper;

import dev.capacytor.forms.configuration.CapacytorNetworkProperties;
import dev.capacytor.forms.entity.FormResponse;
import dev.capacytor.forms.entity.shared.PaymentData;
import dev.capacytor.forms.model.FormResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.hateoas.Link;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class FormResponseMapper {
    private CapacytorNetworkProperties capacytorNetworkProperties;

    public void withProperties(CapacytorNetworkProperties networkProperties) {
        this.capacytorNetworkProperties = networkProperties;
    }

    public abstract FormResponseDto formResponseToFormResponseDto(FormResponse formResponse);

    @AfterMapping
    public void afterFormResponse(@MappingTarget FormResponseDto formResponseDto, FormResponse formResponse) {
        formResponseDto
                .add(Link
                        .of(this.capacytorNetworkProperties.getBase() +
                                "/form/responses/" +
                                formResponse.getId()).withSelfRel().withType("application/json"));

        if (formResponse.getPaymentData() != null &&
                formResponse.getPaymentData().getPaymentUrl() != null &&
                formResponse.getPaymentData().getPaymentStatus().equals(PaymentData.PaymentStatus.PENDING)
        ) {
            formResponseDto
                    .add(Link.of(formResponse.getPaymentData().getPaymentUrl())
                            .withRel("payment")
                            .withType("application/json")
                    );
        }
    }
}
