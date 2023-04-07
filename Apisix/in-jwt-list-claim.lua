-- Plugin to validate a JWT list claim has a required value.
-- It requires the JWT token to be in the 'authorization' header.
-- For example a JWT claim like this:
-- {
--   iss: "me",
--   roles: ["CONFIG_ADMIN", "ACTIVITY_VIEWER"] 
-- }
-- Can be used to validate routes in ingress controller like this:
-- - name: only-admin
--   match:
--     paths:
--     - /onlyadmin
--   backends:
--   - serviceName: theservice
--     servicePort: 8080
--   authentication:
--     enable: true
--     type: jwtAuth
--   plugins:
--   - name: in-jwt-list-claim
--     enable: true
--     config:
--       claim_name: roles
--       method_reqs:
--       - method: ANY
--         one_of_these: ["CONFIG_ADMIN"]
local plugin_name = "in-jwt-list-claim"

-- define the schema for the Plugin
local schema = {
    type = "object",
    required = {"claim_name", "method_reqs"},
    properties = {
        claim_name = {
            description = "name of JWT claim that has the list of values",
            type = "string",
            minLength = 1
        },
        method_reqs = {
            description = "one of these values must be in the claim values",
            type = "array",
            minItems = 1,
            items = {
                type = "object",
                required = {"method", "one_of_these"},
                properties = {
                    method = {
                        type = "string",
                        enum = {"ANY", "GET", "POST", "PUT", "DELETE"},
                    },
                    one_of_these = {
                        type = "array",
                        minItems = 1,
                        items =  {
                            type = "string",
                            minLength = 1
                        }
                    }
                }
            }
        }
    }
}

-- custom Plugins usually have priority between 1 and 99. Higher number = higher priority
local _M = {
    version = 1.0,
    priority = 50,
    name = plugin_name,
    schema = schema,
}

-- dependencies
local core = require("apisix.core")
local jwt = require("resty.jwt")
local ngx = ngx

-- verify the specification
function _M.check_schema(conf)
    local ok, err = core.schema.check(schema, conf)
    if not ok then
        return false, err
    end
    return true
end

local function fetch_jwt_object(conf, ctx)
    local token = core.request.header(ctx, "authorization")
    if token then
        local prefix = string.sub(token, 1, 7)
        if prefix == 'Bearer ' or prefix == 'bearer ' then
            token = sub_str(token, 8)
        end
        return jwt:load_jwt(token)
    end
    return nil
end

local function fetch_list_claim_value(jwt_obj, conf)
    local claim_value = jwt_obj.payload[conf.claim_name]
    if not claim_value or type(claim_value) ~= "table" or #claim_value < 1 then
        return nil
    else
        return claim_value
    end
end

local function has_value(tab, val)
    for index, value in ipairs(tab) do
        if value == val then
            return true
        end
    end
    return false
end

local function is_value_in_claim(request_method, list_in_claim, conf)
    local request_method_lower = string.lower(request_method)
    for index, method_req in ipairs(conf.method_reqs) do
        local method_lower = string.lower(method_req.method)
        if method_lower == "any" or method_lower == request_method_lower then
            for index, value in ipairs(method_req.one_of_these) do
                if has_value(list_in_claim, value) then
                    return true
                end
            end
        end
    end
    return false
end

-- run the Plugin
function _M.rewrite(conf, ctx)
    local jwt_obj = fetch_jwt_object(conf, ctx)
    if not jwt_obj or not jwt_obj.valid then
        return core.response.exit(401, "JWT not found or not valid")
    end

    local list_in_claim = fetch_list_claim_value(jwt_obj, conf)
    if not list_in_claim then
        return core.response.exit(401, "Required claim not found or not valid")
    end

    local request_method = ngx.req.get_method()
    if not is_value_in_claim(request_method, list_in_claim, conf) then
        return core.response.exit(401, "Forbidden")
    end
end

return _M