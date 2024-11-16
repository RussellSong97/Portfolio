package com.cuv.domain.memberadmin;

import java.util.List;

public interface MemberAdminRepositoryCustom {

    List<Long> searchAllMemberDealer(String role);

}
